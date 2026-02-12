plugins {
    alias(libs.plugins.beylearn.android.core)
    alias(libs.plugins.beylearn.hilt)
}

dependencies {
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chuckerNoop)
    implementation(libs.timber)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.bundles.network.implementation)
}