package com.hckim.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hckim.pokedex.core.ui.theme.PokeDexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokeDexActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeDexTheme {
                PokeDexNavGraph()
            }
        }
    }
}
