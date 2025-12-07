plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

// 应用通用库配置
apply(from = "${rootProject.projectDir}/gradles/library.gradle")

android {
    namespace = "com.lzk.core.mmkv"
}

dependencies {
    // MMKV - 高性能键值存储
    implementation(libs.mmkv)
}
