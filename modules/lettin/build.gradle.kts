plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}
apply(from = "${rootProject.projectDir}/gradles/module.gradle")
android {
    namespace = "com.lzk.lettin"
    defaultConfig {
        applicationId = "com.lzk.lettin"
    }
    buildTypes {
        debug {
            buildConfigField("Boolean", "isDebug", "true")
        }
        release {
            buildConfigField("Boolean", "isDebug", "false")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
}
dependencies {
    implementation(project(":business:lettin-main"))
    implementation(project(":business:lettin-device"))
    implementation(project(":business:lettin-user"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
