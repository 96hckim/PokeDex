plugins {
    alias(libs.plugins.pokedex.android.feature)
    alias(libs.plugins.pokedex.android.library.compose)
}

android {
    namespace = "com.hckim.pokedex.feature.favorites"
}

dependencies {
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.lifecycle.runtimeTesting)
}
