dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    // Share Version Catalog dari project utama ke build-logic
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")