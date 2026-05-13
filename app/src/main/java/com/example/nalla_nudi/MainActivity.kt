package com.example.nalla_nudi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nalla_nudi.ui.screens.NavGraph
import com.example.nalla_nudi.ui.theme.NallaNudiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NallaNudiTheme {
                NavGraph()
            }
        }
    }
}