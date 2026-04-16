plugins {
    alias(libs.plugins.pokedex.android.library)
    alias(libs.plugins.pokedex.android.library.compose)
}

android {
    namespace = "com.hckim.pokedex.designsystem"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)

    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.network.okhttp)

    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.hilt.android.testing)
}
