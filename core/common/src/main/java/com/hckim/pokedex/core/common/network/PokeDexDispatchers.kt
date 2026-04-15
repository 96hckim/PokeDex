package com.hckim.pokedex.core.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val pokeDexDispatcher: PokeDexDispatchers)

enum class PokeDexDispatchers {
    Default,
    IO,
}
