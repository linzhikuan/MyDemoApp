plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

// 应用通用库配置
apply(from = "${rootProject.projectDir}/gradles/common.gradle")

android {
    namespace = "com.lzk.common.service"
}

dependencies {
    api(libs.arouter.api) {
        exclude(group = "com.android.support")
    }
    api(project(":common:bean"))
}
