package com.openmoney.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.openmoney.app.ui.OpenMoneyApp
import com.openmoney.app.ui.theme.OpenMoneyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenMoneyTheme {
                OpenMoneyApp()
            }
        }
    }
}
