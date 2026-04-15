plugins {
    alias(libs.plugins.pokedex.jvm.library)
    alias(libs.plugins.pokedex.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}
