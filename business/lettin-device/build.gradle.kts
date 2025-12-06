plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

// 应用通用库配置
apply(from = "${rootProject.projectDir}/gradles/business.gradle")

android {
    namespace = "com.lzk.demo.business.lettin.device"
}

dependencies {
    implementation(project(":core:socket"))
}
