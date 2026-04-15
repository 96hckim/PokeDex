plugins {
    alias(libs.plugins.pokedex.jvm.library)
}

dependencies {
    api(projects.core.model)

    implementation(libs.paging.common)
}
