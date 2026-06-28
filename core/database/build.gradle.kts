plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

// 应用通用库配置
apply(from = "${rootProject.projectDir}/gradles/library.gradle")

android {
    namespace = "com.lzk.core.database"
}

dependencies {
    // Room dependencies
    api(libs.room.runtime)
    api(libs.room.ktx)
}
