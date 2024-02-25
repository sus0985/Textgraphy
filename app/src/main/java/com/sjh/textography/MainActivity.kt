package com.sjh.textography

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.sjh.textography.ui.theme.TextographyTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel> { MainViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextographyTheme {
                MainScreen(viewModel)
            }
        }
    }
}