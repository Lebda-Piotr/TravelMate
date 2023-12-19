pluginManagement {
    repositories {
        google()
        jcenter()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://clojars.org/repo")
        maven(url = "https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "TravelMate"
include(":app")

