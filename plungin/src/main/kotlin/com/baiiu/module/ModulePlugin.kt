package com.baiiu.module

import com.android.build.gradle.AppExtension
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File


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

        if (!project.plugins.hasPlugin("com.android.application")) {
            throw GradleException("ModulePlugin must be used in Android Application plugin")
        }

//        project.extensions.add("module", ModuleExtension::class.java)
//        val moduleExtension: ModuleExtension = project.extensions.getByType(ModuleExtension::class.java)
//        println("moduleExtension: " + moduleExtension.modules)

        val assembleTask = isAssemble(project.gradle.startParameter.taskNames)
        println("taskNames: " + assembleTask + ", " + project.gradle.startParameter.taskNames)


        val transfrom = ModuleTransform()


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
//            val moduleFile = File(project.projectDir, "module.properties")
//            if (!moduleFile.exists()) {
//                return@afterEvaluate
//            }
//
//            val prop = Properties()
//            prop.load(FileInputStream(moduleFile))
//            val implementModule = prop.getProperty("implementModule")
//            println("module.properties: $implementModule")
//
//            val runtimeOnlyConfiguration = project.configurations.getByName("runtimeOnly")
//            for (dependency in runtimeOnlyConfiguration.dependencies) {
//                println("implementationConfiguration: " + runtimeOnlyConfiguration.dependencies + ", " + dependency)
//            }
//
//
//            implementModule.split(",").forEach {
//                it.replace("'", "").let { module ->
//                    println("addImplementation: $module")
//                    runtimeOnlyConfiguration.dependencies.add(project.dependencies.create(project.project(module.trim())))
//                }
//            }

            // json配置

            val runtimeOnlyConfiguration = project.configurations.getByName("runtimeOnly")
            for (dependency in runtimeOnlyConfiguration.dependencies) {
                println("implementationConfiguration: " + runtimeOnlyConfiguration.dependencies + ", " + dependency)
            }

            val jsonFile = File(project.rootDir, "runalone.json")
            if (!jsonFile.exists()) {
                return@afterEvaluate
            }
            val json = jsonFile.readText()
            println("json: $json")

            val list: List<RunAlone> = Gson().fromJson(json, object : TypeToken<List<RunAlone?>?>() {}.type)
            println("runAloneList: $list")

            list.forEach { item ->
                if (project.name.contains(item.module, true)) {
                    transfrom.mRunalone = item

                    item.implement.forEach {
                        println("addImplementation: $it")
                        runtimeOnlyConfiguration.dependencies.add(project.dependencies.create(project.project(it)))
                    }
                }
            }

        }

        android.registerTransform(transfrom)
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