package com.virgil.composedemo.weigth

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PullToRefreshCustomStyleSample(paddingValues: PaddingValues){
    var isRefreshing by remember { mutableStateOf(false) }
    var dataList by remember { mutableStateOf(listOf("Item1", "Item2", "item3")) }

    LazyColumn(modifier = Modifier.fillMaxSize(1f).padding(paddingValues)) {
        items(dataList){ item ->
            Text(text = item, modifier = Modifier.padding(16.dp))
        }
    }

    if (isRefreshing){
        CircularProgressIndicator(color = Color.Blue)
    }
}