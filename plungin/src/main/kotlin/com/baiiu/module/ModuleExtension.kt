package com.baiiu.module

/**
 * author: zhuzhe
 * time: 2020-01-09
 * description:
 */
open class ModuleExtension {

    val modules = mutableSetOf<String>()


    open fun implementModule(vararg string: String) {
        println("implementModule --> $string")

        modules.addAll(string)

        println("implementModule --> $modules")
    }

}