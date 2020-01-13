package com.baiiu.module

import com.android.build.api.transform.*
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * author: zhuzhe
 * time: 2020-01-09
 * description:
 */
class ModuleTransform : Transform() {
    var mRunalone: RunAlone? = null


    override fun getName(): String {
        return "CustomTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(QualifiedContent.DefaultContentType.CLASSES, QualifiedContent.DefaultContentType.RESOURCES)
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(
                QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES,
                QualifiedContent.Scope.SUB_PROJECTS
        )
    }


    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)

        println("==============================TracePlugin visit start========================================")
        val isIncremental = transformInvocation?.isIncremental

        //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        val outputProvider = transformInvocation?.outputProvider

        if (isIncremental == false) {
            //不需要增量编译，先清除全部
            outputProvider?.deleteAll()
        }

        transformInvocation?.inputs?.forEach { input ->
            input?.jarInputs?.forEach { jarInput ->
                //处理Jar
                processJarInput(jarInput, outputProvider, isIncremental)
            }

            input.directoryInputs.forEach { directoryInput ->
                //处理文件
                processDirectoryInput(directoryInput, outputProvider, isIncremental)
            }
        }
        println("==============================TracePlugin visit end========================================")
    }

    //jar输入文件 修改
    private fun processJarInput(jarInput: JarInput, outputProvider: TransformOutputProvider?, isIncremental: Boolean?) {

        var dest = outputProvider?.getContentLocation(
                jarInput.file.absolutePath,
                jarInput.contentTypes,
                jarInput.scopes,
                Format.JAR)
        if (isIncremental == true) {
            //处理增量编译
            processJarInputIncremental(jarInput, dest)
        } else {
            //不处理增量编译
            processJarInputNoIncremental(jarInput, dest)
        }
    }

    //jar 没有增量的修改
    private fun processJarInputNoIncremental(jarInput: JarInput, dest: File?) {
        transformJarInput(jarInput, dest)
    }


    //jar 增量的修改
    private fun processJarInputIncremental(jarInput: JarInput, dest: File?) {
        when (jarInput.status) {
            Status.NOTCHANGED -> {
            }

            Status.ADDED -> {
                //真正transform的地方
                transformJarInput(jarInput, dest)
            }
            Status.CHANGED -> {
                //Changed的状态需要先删除之前的
                if (dest?.exists() == true) {
                    FileUtils.forceDelete(dest)
                }
                //真正transform的地方
                transformJarInput(jarInput, dest)
            }
            Status.REMOVED ->
                //移除Removed
                if (dest?.exists() == true) {
                    FileUtils.forceDelete(dest)
                }
        }
    }

    //真正执行jar修改的函数
    private fun transformJarInput(jarInput: JarInput, dest: File?) {

        println("JarInputName: ${jarInput.file.name}, ${jarInput.file.absolutePath};" + " dest: ${dest?.name}, ${dest?.absolutePath}")

        if (jarInput.file.absolutePath.endsWith(".jar")) {
            //重名名输出文件,因为可能同名,会覆盖
            var jarName = jarInput.name
            val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length - 4)
            }

            val jarFile = JarFile(jarInput.file)
            val enumeration = jarFile.entries()
            val tmpFile = File(jarInput.file.parent + File.separator + "classes_temp.jar")
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            //
            val jarOutputStream = JarOutputStream(FileOutputStream(tmpFile))
            while (enumeration.hasMoreElements()) {
                val jarEntry = enumeration.nextElement()
                val entryName = jarEntry.name
                val zipEntry = ZipEntry(entryName)
                val inputStream = jarFile.getInputStream(jarEntry)
                //插桩class
                if (checkClassFile(entryName)) {
                    jarOutputStream.putNextEntry(zipEntry)
                    val classReader = ClassReader(IOUtils.toByteArray(inputStream))
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    val cv = ModuleVisitor(classWriter, mRunalone)
                    classReader.accept(cv, ClassReader.EXPAND_FRAMES)
                    val code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }

                jarOutputStream.closeEntry()
            }

            //结束
            jarOutputStream.close()
            jarFile.close()
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }

    }


    private fun processDirectoryInput(directoryInput: DirectoryInput, outputProvider: TransformOutputProvider?, isIncremental: Boolean?) {
        var dest = outputProvider?.getContentLocation(
                directoryInput.file.absolutePath,
                directoryInput.contentTypes,
                directoryInput.scopes,
                Format.DIRECTORY)
        if (isIncremental == true) {
            //处理增量编译
            processDirectoryInputIncremental(directoryInput, dest!!)
        } else {
            processDirectoryInputNoIncremental(directoryInput, dest)
        }
    }

    //文件无增量修改
    private fun processDirectoryInputNoIncremental(directoryInput: DirectoryInput, dest: File?) {
        transformDirectoryInput(directoryInput, dest)
    }

    //文件增量修改
    private fun processDirectoryInputIncremental(directoryInput: DirectoryInput, dest: File) {
        FileUtils.forceMkdir(dest)
        val srcDirPath = directoryInput.file.absolutePath
        val destDirPath = dest.absolutePath
        val fileStatusMap = directoryInput.changedFiles
        fileStatusMap.forEach { entry ->
            val inputFile = entry.key
            val status = entry.value
            val destFilePath = inputFile.absolutePath.replace(srcDirPath, destDirPath)
            val destFile = File(destFilePath)

            when (status) {
                Status.NOTCHANGED -> {
                }

                Status.ADDED -> {
                    //真正transform的地方
                    transformDirectoryInput(directoryInput, dest)
                }

                Status.CHANGED -> {
                    //处理有变化的
                    FileUtils.touch(destFile)
                    //Changed的状态需要先删除之前的
                    if (dest.exists()) {
                        FileUtils.forceDelete(dest)
                    }
                    //真正transform的地方
                    transformDirectoryInput(directoryInput, dest)
                }

                Status.REMOVED ->
                    //移除Removed
                    if (destFile.exists()) {
                        FileUtils.forceDelete(destFile)
                    }
            }
        }
    }

    //真正执行文件修改的地方
    private fun transformDirectoryInput(directoryInput: DirectoryInput, dest: File?) {
        //是否是目录
        if (directoryInput.file?.isDirectory == true) {
            val fileTreeWalk = directoryInput.file.walk()
            fileTreeWalk.forEach { file ->
                val name = file.name

                println("DirectoryInputName: ${file.name}, file is: ${file.absolutePath}")

                //在这里进行代码处理
                if (name.endsWith(".class") && !name.startsWith("R\$")
                        && "R.class" != name && "BuildConfig.class" != name) {

                    val classReader = ClassReader(file.readBytes())
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)

                    val classVisitor = ModuleVisitor(classWriter, mRunalone)
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                    val code = classWriter.toByteArray()
                    val fos = FileOutputStream(file.parentFile.absoluteFile.toString() + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }

        //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private fun transformSingleFile(inputFile: File, destFile: File, srcDirPath: String) {
        FileUtils.copyFile(inputFile, destFile)
    }

    private fun checkClassFile(name: String): Boolean {
        //只处理需要的class文件
        return (name.endsWith(".class")
                && !name.startsWith("R\$")
                && "R.class" != name
                && "BuildConfig.class" != name
                && !name.startsWith("android/support"))
    }

}