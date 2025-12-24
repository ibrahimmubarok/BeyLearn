plugins {
    alias(libs.plugins.beylearn.android.api)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core-entity"))
}