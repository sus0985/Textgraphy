package com.sjh.textography

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    steganographyManager: SteganographyManager
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
        val context = LocalContext.current

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    val bitmap = BitmapUtil.loadBitmapFromUri(it, context)
                    selectedBitmap = bitmap
                }
            }
        )

        Button(
            onClick = {
                val mediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                imagePickerLauncher.launch(mediaRequest)
            }
        ) {
            Text("Pick Image")
        }

        selectedBitmap?.let { bitmap ->
            Text(text = "Selected Image")

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .aspectRatio(1.2f)
            )

            var text by remember { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = {
                    text = it
                }
            )

            var embeddedBitmap by remember { mutableStateOf<Bitmap?>(null) }

            Button(onClick = {
                embeddedBitmap = steganographyManager.embedTextIntoBitmap(bitmap, text)
            }) {
                Text("Embedding text")
            }

            embeddedBitmap?.let { bitmap ->
                Text("Embedded Image")
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .aspectRatio(1.2f)
                )
            }


        }
    }
}