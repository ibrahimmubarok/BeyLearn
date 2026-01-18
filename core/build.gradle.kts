plugins {
    alias(libs.plugins.beylearn.android.core)
}

dependencies {
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker)
}