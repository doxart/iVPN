buildscript {
    repositories {
        google()
        mavenCentral()
        maven (url = "https://maven.google.com")
        maven ( url = "https://jitpack.io" )
        maven (url = "https://repository-achartengine.forge.cloudbees.com/snapshot/")
    }
}

plugins {
    id("com.android.application") version "8.0.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}