plugins {
    alias(libs.plugins.pokedex.android.library)
    alias(libs.plugins.pokedex.android.room)
    alias(libs.plugins.pokedex.hilt)
}

android {
    namespace = "com.hckim.pokedex.core.data"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.domain)
    api(projects.core.network)
    api(projects.core.model)

    implementation(libs.paging.runtime)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
}
