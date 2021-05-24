plugins {
    kotlin("jvm") version "1.5.0"
}

allprojects {
    group = "aws.sdk.kotlin.example"
    version = "0.3.0-SNAPSHOT"

    repositories {
        maven {
            name = "kotlinSdkLocal"
            url = uri(TODO("set your local repository path"))
            // e.g.
            //url = uri("file:///tmp/aws-sdk-kotlin-repo/m2")
        }
        mavenCentral()
    }
}
