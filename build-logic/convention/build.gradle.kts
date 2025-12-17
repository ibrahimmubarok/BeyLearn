plugins {
    `kotlin-dsl`
}

group = "com.ibeybeh.buildLogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)

    implementation(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApi") {
            id = "beylearn.android.api"
            implementationClass = "com.ibeybeh.buildLogic.BeylearnApiConventionPlugin"
        }
        register("androidCore") {
            id = "beylearn.android.core"
            implementationClass = "com.ibeybeh.buildLogic.BeylearnCoreConventionPlugin"
        }
        register("androidFeature") {
            id = "beylearn.android.feature"
            implementationClass = "com.ibeybeh.buildLogic.BeylearnFeatureConventionPlugin"
        }
    }
}