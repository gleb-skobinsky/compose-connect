rootProject.name = "Chirrio"

include(":androidApp")
include(":shared")
include(":desktopApp")
include(":jsApp")
include(":filepicker")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
include(":cloudinary")
