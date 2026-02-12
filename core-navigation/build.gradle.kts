plugins {
    alias(libs.plugins.beylearn.android.core)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.beylearn.hilt)
}

dependencies {
    implementation(project(":core-entity"))
    implementation(libs.compose.navigation)
    implementation(libs.kotlinx.serialization)
}