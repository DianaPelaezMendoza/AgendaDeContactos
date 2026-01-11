package com.example.principal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.principal.ui.theme.AgendaDeContactosTheme
import com.example.principal.ui.theme.NavigationHostController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgendaDeContactosTheme {
                NavigationHostController()
            }
        }
    }
}

