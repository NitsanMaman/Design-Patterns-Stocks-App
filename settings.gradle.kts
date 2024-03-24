pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io") // Add this line for plugins if needed
        gradlePluginPortal()
    }
}

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        // classpath for plugins, for example:
        // classpath("com.android.tools.build:gradle:VERSION")
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
        maven ( url = "https://jitpack.io" ) //Add this line in your settings.gradle
    }
}

rootProject.name = "TradingView"
include(":app")
