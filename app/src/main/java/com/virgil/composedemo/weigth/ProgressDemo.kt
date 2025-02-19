package com.virgil.composedemo.weigth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProgressDemo(paddingValues: PaddingValues){
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(paddingValues)
    ) {
        LinearDeterMinateIndicator(paddingValues, "LinearProgress")

        LinearDeterMinateIndicator(paddingValues, "CircularProgress")

        IndeterminateIndicator(paddingValues, "Circular")

        IndeterminateIndicator(paddingValues, "Linear")
    }
}

@Composable
fun LinearDeterMinateIndicator(paddingValues: PaddingValues, type: String){
    var currentProgress by remember { mutableStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    var scope = rememberCoroutineScope()

    Column{
        Button(onClick = {
            loading = true
            scope.launch {
                loadProgress { progress ->
                    currentProgress = progress
                }
                loading = false
            }
        }, enabled = !loading){
            Text("Start loading")
        }

        if (loading){
            if (type == "LinearProgress"){
                LinearProgressIndicator(
                    progress = { currentProgress },
                    modifier = Modifier.fillMaxWidth().padding(12.dp)
                )
            }
            if (type == "CircularProgress"){
                CircularProgressIndicator(
                    progress = { currentProgress },
                    modifier = Modifier.width(50.dp)
                )
            }
        }
    }
}

@Composable
fun IndeterminateIndicator(paddingValues: PaddingValues, type:String){
    var loading by remember { mutableStateOf(false) }

    Button(onClick = { loading = true }, enabled = !loading){
        Text("Start loading")
    }

    if (!loading) return

    if (type == "Circular"){
        CircularProgressIndicator(
            modifier = Modifier.width(50.dp).padding(12.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }

   if (type == "Linear"){
       LinearProgressIndicator(
           modifier = Modifier.fillMaxWidth().padding(12.dp, 0.dp),

           color = MaterialTheme.colorScheme.secondary,
           trackColor = MaterialTheme.colorScheme.surfaceVariant
       )
   }
}

suspend fun loadProgress(updateProgress: (Float) -> Unit){
    for (i in 1..100){
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}