plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// 应用通用库配置
apply(from = "${rootProject.projectDir}/gradles/business.gradle")

android {
    namespace = "com.lzk.demo.business.lettin.main"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(project(":business:lettin-device"))
    implementation(project(":business:lettin-user"))
}
