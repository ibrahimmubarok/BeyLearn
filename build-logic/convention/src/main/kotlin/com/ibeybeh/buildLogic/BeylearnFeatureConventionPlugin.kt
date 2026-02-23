package com.ibeybeh.buildlogic

import com.android.build.gradle.LibraryExtension
import com.ibeybeh.buildlogic.utils.BundleNames.LIBS
import com.ibeybeh.buildlogic.utils.BundleNames.PROJECT_APPLICATION_ID
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class BeyLearnFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named(LIBS)

            val baseNamespace = libs.findVersion(PROJECT_APPLICATION_ID).get().toString()

            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<LibraryExtension> {
                val moduleSuffix = path.removePrefix(":")
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
                    compose = true
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
                add("implementation", project(":core"))
                add("implementation", project(":core-entity"))
                add("implementation", project(":core-ui"))
                add("implementation", project(":core-navigation"))
                add("ksp", project(":navigation-processor"))
            }
        }
    }
}