plugins {
    alias(libs.plugins.pokedex.jvm.library)
}

dependencies {
    api(projects.core.model)

    implementation(libs.javax.inject)

    implementation(libs.paging.common)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
}
