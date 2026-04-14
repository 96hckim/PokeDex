package com.hckim.pokedex.domain.model

import androidx.compose.ui.graphics.Color
import com.hckim.pokedex.ui.theme.TypeBug
import com.hckim.pokedex.ui.theme.TypeDragon
import com.hckim.pokedex.ui.theme.TypeElectric
import com.hckim.pokedex.ui.theme.TypeFairy
import com.hckim.pokedex.ui.theme.TypeFighting
import com.hckim.pokedex.ui.theme.TypeFire
import com.hckim.pokedex.ui.theme.TypeFlying
import com.hckim.pokedex.ui.theme.TypeGhost
import com.hckim.pokedex.ui.theme.TypeGrass
import com.hckim.pokedex.ui.theme.TypeGround
import com.hckim.pokedex.ui.theme.TypeIce
import com.hckim.pokedex.ui.theme.TypeNormal
import com.hckim.pokedex.ui.theme.TypePoison
import com.hckim.pokedex.ui.theme.TypePsychic
import com.hckim.pokedex.ui.theme.TypeRock
import com.hckim.pokedex.ui.theme.TypeSteel
import com.hckim.pokedex.ui.theme.TypeUnknown
import com.hckim.pokedex.ui.theme.TypeWater
import kotlinx.serialization.Serializable

@Serializable
enum class PokemonType(
    val displayName: String,
    val color: Color
) {
    NORMAL("Normal", TypeNormal),
    FIRE("Fire", TypeFire),
    WATER("Water", TypeWater),
    ELECTRIC("Electric", TypeElectric),
    GRASS("Grass", TypeGrass),
    ICE("Ice", TypeIce),
    FIGHTING("Fighting", TypeFighting),
    POISON("Poison", TypePoison),
    GROUND("Ground", TypeGround),
    FLYING("Flying", TypeFlying),
    PSYCHIC("Psychic", TypePsychic),
    BUG("Bug", TypeBug),
    ROCK("Rock", TypeRock),
    GHOST("Ghost", TypeGhost),
    DRAGON("Dragon", TypeDragon),
    STEEL("Steel", TypeSteel),
    FAIRY("Fairy", TypeFairy),
    UNKNOWN("Unknown", TypeUnknown);

    companion object {
        fun fromString(type: String): PokemonType {
            return entries.find { it.name.equals(type, ignoreCase = true) } ?: UNKNOWN
        }
    }
}
