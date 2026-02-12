plugins {
    alias(libs.plugins.beylearn.android.core)
    alias(libs.plugins.beylearn.android.compose)
    alias(libs.plugins.beylearn.hilt)
}

dependencies {
    implementation(project(":core-entity"))
}