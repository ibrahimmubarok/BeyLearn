package com.ibeybeh.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class BeyLearnComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                val bom = libs.findLibrary("androidx-compose-bom").get()

                // BOM Platform
                add("implementation", platform(bom))
                add("androidTestImplementation", platform(bom))

                // Core Dependencies
                add("implementation", libs.findLibrary("androidx-ui").get())
                add("implementation", libs.findLibrary("androidx-ui-graphics").get())
                add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())
                add("implementation", libs.findLibrary("androidx-material3").get())

                // Debug & Testing
                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx-ui-test-junit4").get()
                )
                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx-espresso-core").get()
                )
                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx-junit").get()
                )
                add("debugImplementation", libs.findLibrary("androidx-ui-tooling").get())
                add(
                    "debugImplementation",
                    libs.findLibrary("androidx-ui-test-manifest").get()
                )
            }
        }
    }
}