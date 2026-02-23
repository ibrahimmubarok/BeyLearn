plugins {
    `kotlin-dsl`
}

group = "com.ibeybeh.buildLogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)

    implementation(libs.ksp.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApi") {
            id = "beylearn.android.api"
            implementationClass = "com.ibeybeh.buildlogic.BeyLearnApiConventionPlugin"
        }
        register("androidCore") {
            id = "beylearn.android.core"
            implementationClass = "com.ibeybeh.buildlogic.BeyLearnCoreConventionPlugin"
        }
        register("androidFeature") {
            id = "beylearn.android.feature"
            implementationClass = "com.ibeybeh.buildlogic.BeyLearnFeatureConventionPlugin"
        }
        register("hildConvention") {
            id = "beylearn.hilt"
            implementationClass = "com.ibeybeh.buildlogic.BeyLearnHiltConventionPlugin"
        }
        register("androidCompose") {
            id = "beylearn.android.compose"
            implementationClass = "com.ibeybeh.buildlogic.BeyLearnComposeConventionPlugin"
        }
    }
}