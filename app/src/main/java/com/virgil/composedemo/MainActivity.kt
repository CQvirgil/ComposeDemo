package com.virgil.composedemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.virgil.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
val demoList = listOf("Button", "卡片", "复选框", "条状标签", "固定的日期选择器")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(demoList.get(0)) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {//抽屉区域
            ModalDrawerSheet {
                LazyColumn {
                   items(demoList.size){ i ->
                        TextButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                           showContent = demoList.get(i)
                        }){
                            Text(demoList.get(i))
                        }
                   }
                }
            }
        },
    ) {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Show drawer") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        showBottomSheet = true
                    }
                )
            },
            topBar = {
                CenterAlignedTopAppBar(title = { Text(showContent) }, navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = ""
                        )
                    }
                })
            }
        ) { contentPadding->
            // Screen content
//            ButtomDemo(contentPadding)
            if (showContent == demoList[0]){
                ButtomDemo(contentPadding)
            } else if (showContent == demoList[1]){
                CardDemo(contentPadding)
            } else if (showContent == demoList[2]){
                CheckboxDemo(contentPadding)
            } else if (showContent == demoList[3]){
                FilterChipDemo(contentPadding)
            } else if (showContent == demoList[4]){
                DataPickerDocked(contentPadding)
            } else {
                Text("Screen Content", modifier = Modifier.padding(contentPadding))
            }
            if (showBottomSheet){
                ModalBottomSheet(onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState){
                    Button(onClick = {//隐藏底部弹窗
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible){
                                showBottomSheet = false
                            }
                        }
                    }){
                        Text("Hide bottom sheet")
                    }
                }
            }
        }
    }
}

@Composable
fun CheckboxParentExample(){
    val childCheckedStates = remember { mutableStateListOf(false, false, false) }

    val parentState = when {
        childCheckedStates.all { it } -> ToggleableState.On
        childCheckedStates.none { it } -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Select all")
            TriStateCheckbox(
                state = parentState,
                onClick = {
                    val newState = parentState != ToggleableState.On
                    childCheckedStates.forEachIndexed { index, b ->
                        childCheckedStates[index] = newState
                    }
                }
            )
        }

        childCheckedStates.forEachIndexed{ index, checked ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Option ${index + 1}")
                Checkbox(
                    checked = checked,
                    onCheckedChange = { isChecked->
                        childCheckedStates[index] = isChecked
                    }
                )
            }
        }

        if (childCheckedStates.all { it }){
            Text("All options selected")
        }
    }
}

@Composable
fun InputChipExample(
    text: String,
    onDissmiss: () -> Unit
){
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(onClick = {
        onDissmiss()
        enabled = !enabled
    }, label = { Text(text) },
        selected = enabled,
        avatar = {
            Icon(
                Icons.Filled.Person,
                contentDescription = "Localized description",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
        modifier = Modifier.padding(16.dp),
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "Localized description",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        })
}

@Composable
fun AssistChipExample(){
    AssistChip(
        onClick = { Log.d("Assist chip", " hello world") },
        label = { Text("Assist chip") },
        modifier = Modifier.padding(16.dp),
        leadingIcon = {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Localized description",
                Modifier.size(AssistChipDefaults.IconSize)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataPickerDocked(paddingValues: PaddingValues){
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        showDatePicker = false
        convertMillisToDate(it)
    }

    Box(
        modifier = Modifier.fillMaxWidth().padding(paddingValues)
    ) {
        OutlinedTextField(
            value = selectedDate.toString(),
            onValueChange = {},
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {showDatePicker = !showDatePicker}){
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date",
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp)
        )

        if (showDatePicker){
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(modifier = Modifier.fillMaxWidth()
                    .offset(y = 64.dp)
                    .shadow(elevation = 4.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)){
                    DatePicker(state = datePickerState, showModeToggle = false, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier){
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = {},
        label = { Text("DOB") },
        placeholder = { Text("yyyy-MM-dd") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier.fillMaxWidth()
            .pointerInput(selectedDate){
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null){
                        showModal = true
                    }
                }
            }
    )

    if (showModal){
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected:(Long?) -> Unit,
    onDismiss: () -> Unit
){
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }){
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = onDismiss){
                Text("Cancel")
            }
        }
    ){
        DatePicker(state = datePickerState)
    }
}

@Composable
fun FilterChipExample(){
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = {selected = !selected},
        label = {
            Text("Filter chip")
        },
        modifier = Modifier.padding(16.dp),
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Composable
fun FilterChipDemo(contentPadding: PaddingValues){
    Column(modifier = Modifier.padding(contentPadding)) {
        FilterChipExample()
        AssistChipExample()
        InputChipExample("test"){
            Log.d("Debug-Chip", "FilterChipDemo: ")
        }
    }
}

@Composable
fun CheckboxDemo(contentPadding: PaddingValues){
    Column(modifier = Modifier.padding(contentPadding)) {
        CheckboxMinimalExample()
        CheckboxParentExample()
    }
}

@Composable
fun CheckboxMinimalExample(){
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Minimal checkbox")
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}

@Composable
fun CardDemo(contentPadding: PaddingValues){
    Column(modifier = Modifier.padding(contentPadding)) {
        Card(modifier = Modifier.size(240.dp, height = 100.dp).padding(16.dp)) {
            Text(text = "Card基本实现", textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
        }
        FilledCardExample()
        ElevatedCardExample()
        OutLineCardExample()
    }
}

@Composable
fun FilledCardExample(){//填充卡片
    Card(colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ), modifier = Modifier.size(240.dp, height = 100.dp).padding(16.dp)) {
        Text(text = "Filled 填充卡片", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
    }
}

@Composable
fun ElevatedCardExample(){//上层卡片
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier.size(width = 240.dp, height = 100.dp).padding(16.dp)
    ) {
        Text(text = "Elevated 悬浮卡片", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
    }
}

@Composable
fun OutLineCardExample(){//轮廓卡片
    OutlinedCard(colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    ), border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier.size(width = 240.dp, height = 100.dp).padding(16.dp)
    ) {
        Text(text = "Outlined 轮廓卡片", modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
    }
}

@Composable
fun ButtomDemo(contentPadding: PaddingValues){
    Column(modifier = Modifier.padding(contentPadding)) {
        Button(onClick = {}, modifier = Modifier.padding(16.dp)){ Text("Filled 填充按钮（默认）") }//填充按钮（默认）
        FilledTonalButton(onClick = {}, modifier = Modifier.padding(16.dp)){ Text("Tonal 填充色按钮") }//填充色按钮
        OutlinedButton(onClick = {}, modifier = Modifier.padding(16.dp)){ Text("OutLined 轮廓按钮") }//轮廓按钮
        ElevatedButton(onClick = {}, modifier = Modifier.padding(16.dp)){ Text("Elevated 突起按钮") }//突起按钮
        TextButton(onClick = {}, modifier = Modifier.padding(16.dp)){ Text("Text Button 文本按钮") }//文本按钮
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeDemoTheme {
        Greeting("Android")
    }
}