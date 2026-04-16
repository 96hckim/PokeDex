plugins {
    alias(libs.plugins.pokedex.android.feature)
    alias(libs.plugins.pokedex.android.library.compose)
}

android {
    namespace = "com.hckim.pokedex.feature.detail"
}

dependencies {
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.network.okhttp)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
}
