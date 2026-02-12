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
            implementationClass = "com.ibeybeh.buildlogic.BeylearnApiConventionPlugin"
        }
        register("androidCore") {
            id = "beylearn.android.core"
            implementationClass = "com.ibeybeh.buildlogic.BeylearnCoreConventionPlugin"
        }
        register("androidFeature") {
            id = "beylearn.android.feature"
            implementationClass = "com.ibeybeh.buildlogic.BeylearnFeatureConventionPlugin"
        }
        register("hildConvention") {
            id = "beylearn.hilt"
            implementationClass = "com.ibeybeh.buildlogic.BeylearnHiltConventionPlugin"
        }
        register("androidCompose") {
            id = "beylearn.android.compose"
            implementationClass = "com.ibeybeh.buildlogic.BeylearnComposeConventionPlugin"
        }
    }
}