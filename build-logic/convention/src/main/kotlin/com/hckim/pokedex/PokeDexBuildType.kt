package com.hckim.pokedex

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class PokeDexBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
