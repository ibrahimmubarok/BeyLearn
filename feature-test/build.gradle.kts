plugins {
    alias(libs.plugins.beylearn.android.feature)
    alias(libs.plugins.beylearn.hilt)
    alias(libs.plugins.beylearn.android.compose)
}

dependencies {
    implementation(project(":api-test"))

    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.hilt.navigation.compose)
}
