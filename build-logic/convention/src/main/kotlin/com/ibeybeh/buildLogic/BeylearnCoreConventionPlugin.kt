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
                apply("kotlin-parcelize")
            }

            extensions.configure<LibraryExtension> {
                moduleSuffix = path.removePrefix(":")
                    .replace("-", "_")
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
                    compose = moduleSuffix == "core_ui"
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
            }
        }
    }
}