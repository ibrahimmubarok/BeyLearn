package com.ibeybeh.buildlogic

import com.android.build.gradle.LibraryExtension
import com.ibeybeh.buildlogic.utils.BundleNames.CORE_IMPLEMENTATION
import com.ibeybeh.buildlogic.utils.BundleNames.LIBS
import com.ibeybeh.buildlogic.utils.BundleNames.PROJECT_APPLICATION_ID
import com.ibeybeh.buildlogic.utils.BundleNames.TEST_IMPLEMENTATION
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class BeylearnCoreConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named(LIBS)
            var moduleSuffix = ""

            val baseNamespace = libs.findVersion(PROJECT_APPLICATION_ID).get().toString()

            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
                apply("beylearn.hilt")
            }

            extensions.configure<LibraryExtension> {
                moduleSuffix = path.removePrefix(":")
                    .replace("-", ".")
                    .replace(":", ".")

                namespace = "$baseNamespace.$moduleSuffix"

                compileSdk = 36
                defaultConfig {
                    minSdk = 24
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
                buildFeatures {
                    compose = moduleSuffix == "core-ui"
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                compileOptions {
                    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_17
                    targetCompatibility = org.gradle.api.JavaVersion.VERSION_17
                }
            }

            dependencies {
                libs.findBundle(CORE_IMPLEMENTATION).ifPresent {
                    "implementation"(it)
                }
                libs.findBundle(TEST_IMPLEMENTATION).ifPresent {
                    "implementation"(it)
                }

                if (moduleSuffix == "core.ui") {
                    add("implementation", project(":core-entity"))

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
                    add("debugImplementation", libs.findLibrary("androidx-ui-tooling").get())
                    add("debugImplementation", libs.findLibrary("androidx-ui-test-junit4").get())
                    add("debugImplementation", libs.findLibrary("androidx-ui-test-manifest").get())
                }
            }
        }
    }
}