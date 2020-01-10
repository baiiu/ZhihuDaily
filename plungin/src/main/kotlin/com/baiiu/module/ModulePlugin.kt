package com.baiiu.module

import com.android.build.gradle.AppExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * author: zhuzhe
 * time: 2020-01-09
 * description:
 */
class ModulePlugin : Plugin<Project> {

    var isFirst: Boolean = true

    override fun apply(project: Project) {
        val android = project.extensions.getByType(AppExtension::class.java)


        println("ModulePlugin#apply: " + project.name + ", " + project.displayName)

        project.extensions.add("module", ModuleExtension::class.java)
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw GradleException("ModulePlugin must be used in Android Application plugin")
        }


        val moduleExtension: ModuleExtension = project.extensions.getByType(ModuleExtension::class.java)
        println("moduleExtension: " + moduleExtension.modules)

        project.configurations.all { configuration ->
            val name = configuration.name

            println("this configuration is $name")
            println("dependencies is: " + configuration.dependencies)
            println("allDependencies is: " + configuration.allDependencies)
            println("==========================================================\n")

            if (moduleExtension.modules.isNotEmpty() && isFirst) {
                println("moduleExtension: " + moduleExtension.modules)
                isFirst = false

                for (module in moduleExtension.modules) {
                    println("addImplementation: $module")

                    configuration.dependencies.add(project.dependencies.create(project.project(module)))
                }
            }

            //为Project加入Gson依赖
        }


//        project.configurations.all { configuration ->
//            val name = configuration.name
//            System.out.println("this configuration is $name")
//
//            for (module in moduleExtension.modules) {
//                configuration.dependencies.add(project.dependencies.module(module))
//            }
//
//        }


        android.registerTransform(ModuleTransform())
    }
}