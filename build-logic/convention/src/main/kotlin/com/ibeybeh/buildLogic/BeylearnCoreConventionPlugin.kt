package com.ibeybeh.buildlogic

import com.android.build.gradle.LibraryExtension
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
                apply("kotlin-parcelize")
                apply("beylearn.hilt")
            }

            extensions.configure<LibraryExtension> {
                moduleSuffix = path.removePrefix(":")
                    .replace("-", ".")
                    .replace(":", ".")

                namespace = "$baseNamespace.$moduleSuffix"

                compileSdk = 36
                defaultConfig {
                    minSdk = 26
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
                buildFeatures {
                    buildConfig = true
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
                    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_21
                    targetCompatibility = org.gradle.api.JavaVersion.VERSION_21
                }
            }

            dependencies {
                libs.findBundle(TEST_IMPLEMENTATION).ifPresent {
                    "implementation"(it)
                }

                when (moduleSuffix) {
                    "core.ui" -> {
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
                        add(
                            "debugImplementation",
                            libs.findLibrary("androidx-ui-test-junit4").get()
                        )
                        add(
                            "debugImplementation",
                            libs.findLibrary("androidx-ui-test-manifest").get()
                        )
                    }

                    "core" -> {
                        add("implementation", libs.findLibrary("timber").get())
                        add(
                            "implementation",
                            libs.findLibrary("androidx-lifecycle-runtime-ktx").get()
                        )
                        add("implementation", libs.findLibrary("gson").get())
                        add("implementation", libs.findLibrary("retrofit").get())
                        add("implementation", libs.findLibrary("retrofitGson").get())
                        add("implementation", libs.findLibrary("okhttp").get())
                        add("implementation", libs.findLibrary("okhttpLogging").get())
                    }
                }
            }
        }
    }
}