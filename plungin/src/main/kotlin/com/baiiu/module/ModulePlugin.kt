package com.baiiu.module

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * author: zhuzhe
 * time: 2020-01-09
 * description:
 */
class ModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val android = project.extensions.getByType(AppExtension::class.java)

        android.registerTransform(ModuleTransform())
    }
}