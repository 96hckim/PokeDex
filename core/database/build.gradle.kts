plugins {
    alias(libs.plugins.pokedex.android.library)
    alias(libs.plugins.pokedex.android.room)
    alias(libs.plugins.pokedex.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hckim.pokedex.core.database"
}

dependencies {
    api(projects.core.model)

    implementation(libs.kotlinx.serialization.json)

    androidTestImplementation(libs.kotlinx.coroutines.test)
}
