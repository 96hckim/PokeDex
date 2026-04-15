plugins {
    alias(libs.plugins.pokedex.android.library)
    alias(libs.plugins.pokedex.android.library.compose)
}

android {
    namespace = "com.hckim.pokedex.core.ui"
}

dependencies {
    api(projects.core.model)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.network.okhttp)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
}
