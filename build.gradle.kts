// Top-level build file where you can add configuration options common to all sub-projects/modules.

// 引用项目配置
apply(from = "gradles/projectconfigs.gradle")

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}