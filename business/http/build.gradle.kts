plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

// 应用通用库配置
apply(from = "${rootProject.projectDir}/gradles/business.gradle")

android {
    namespace = "com.lzk.demo.business.http"
}

dependencies {
    implementation(project(":core:network"))
}
