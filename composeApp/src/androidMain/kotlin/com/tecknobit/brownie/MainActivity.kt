package com.tecknobit.brownie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextActivityProvider.setCurrentActivity(this)
        setContent {
            enableEdgeToEdge()
            App()
        }
    }
}