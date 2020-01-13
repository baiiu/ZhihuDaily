package com.baiiu.module

import com.android.build.gradle.AppExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.reflect.TypeOf
import org.gradle.internal.extensibility.ExtensionsStorage
import java.io.File
import java.io.FileInputStream
import java.util.*


/**
 * author: zhuzhe
 * time: 2020-01-09
 * description: 参考：
 *
 * https://github.com/mqzhangw/JIMU/blob/master/jimu-core/build-gradle/src/main/groovy/com.dd.buildgradle/ComBuild.groovy
 */
class ModulePlugin : Plugin<Project> {

//    var isFirst: Boolean = true

    override fun apply(project: Project) {
        val android = project.extensions.getByType(AppExtension::class.java)

        println("ModulePlugin#apply: " + project.name + ", " + project.displayName)

        project.extensions.add("module", ModuleExtension::class.java)
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw GradleException("ModulePlugin must be used in Android Application plugin")
        }

        val moduleExtension: ModuleExtension = project.extensions.getByType(ModuleExtension::class.java)
        println("moduleExtension: " + moduleExtension.modules)

        val assembleTask = isAssemble(project.gradle.startParameter.taskNames)
        println("taskNames: " + assembleTask + ", " + project.gradle.startParameter.taskNames)


        project.afterEvaluate {

            // extension插件配置
//            if (moduleExtension.modules.isNotEmpty() && assembleTask) {
//                println("moduleExtensionAfterEvaluate: " + moduleExtension.modules)
//
//                val runtimeOnlyConfiguration = project.configurations.getByName("runtimeOnly")
//                for (dependency in runtimeOnlyConfiguration.dependencies) {
//                    println("implementationConfiguration: " + runtimeOnlyConfiguration.dependencies + ", " + dependency)
//                }
//
//                for (module in moduleExtension.modules) {
//                    println("addImplementation: $module")
//
//                    runtimeOnlyConfiguration.dependencies.add(project.dependencies.create(project.project(module)))
//                }
//            }


            // 文件配置
            val moduleFile = File(project.projectDir, "module.properties")
            if (!moduleFile.exists()) {
                return@afterEvaluate
            }

            val prop = Properties()
            prop.load(FileInputStream(moduleFile))
            val implementModule = prop.getProperty("implementModule")
            println("module.properties: $implementModule")

            val runtimeOnlyConfiguration = project.configurations.getByName("runtimeOnly")
            for (dependency in runtimeOnlyConfiguration.dependencies) {
                println("implementationConfiguration: " + runtimeOnlyConfiguration.dependencies + ", " + dependency)
            }


            implementModule.split(",").forEach {
                it.replace("'", "").let { module ->
                    println("addImplementation: $module")
                    runtimeOnlyConfiguration.dependencies.add(project.dependencies.create(project.project(module.trim())))
                }
            }


        }

        android.registerTransform(ModuleTransform(project))
    }


    private fun isAssemble(tasks: List<String>): Boolean {

        for (task in tasks) {
            if (
                    task.toUpperCase().contains("ASSEMBLE")
                    || task.toUpperCase().contains("TINKER")
                    || task.toUpperCase().contains("INSTALL")
                    || task.toUpperCase().contains("RESGUARD")
                    || task.contains("aR")
                    || task.contains("asR")
                    || task.contains("asD")) {


                return true
            }
        }

        return false
    }

}