package com.sjh.textography

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sjh.textography.ui.theme.TextographyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextographyTheme {
                MainScreen(SteganographyManager())
            }
        }
    }
}