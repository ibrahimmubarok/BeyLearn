plugins {
    alias(libs.plugins.beylearn.android.core)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.beylearn.hilt)
}

dependencies {
    implementation(libs.kotlinx.serialization)
}