plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.paging.common)
    implementation(libs.kotlinx.coroutines.core)
}
