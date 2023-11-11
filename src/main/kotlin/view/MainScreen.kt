@file:OptIn(ExperimentalFoundationApi::class)

package view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.DashCamFileUseCase
import java.io.File

private const val buttonWidth = 108

@Composable
fun MainScreen(
    useCase: DashCamFileUseCase = DashCamFileUseCase()
) {
//    val uiState = useCase.stateFlow.collectAsState(null).value
    val statusBarMessage = useCase.statusBarStateFlow.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        DriverLoadBar(useCase)

        FootageReaderBar(useCase)

        OutputCopyBar(useCase)

        Row(modifier = Modifier.weight(8.0f)) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
//                uiState?.forEach {
//                    Row {
//                        Text(it.name)
//                    }
//                }
            }
        }

        Row(modifier = Modifier.wrapContentHeight()) {
            statusBarMessage.value?.let {
                Text(it)
            }
        }
    }
}

@Composable
fun DriverLoadBar(useCase: DashCamFileUseCase) {
    val driverExpanded = remember { mutableStateOf(false) }
    val driverState = useCase.driverStateFlow.collectAsState(emptyList())
    val selectedDriver = useCase.selectedDriverStateFlow.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.requiredWidth(buttonWidth.dp),
            onClick = {
                useCase.load()
            }) {
            Text("Search")
        }

        Box {
            OutlinedTextField(
                value = selectedDriver.value?.canonicalPath ?: "",
                onValueChange = { },
                label = { Text("Driver") },
                enabled = driverState.value.isNotEmpty(),
                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "",
                        tint = Color.Black
                    )
                },
                readOnly = true,
                modifier = Modifier.onFocusEvent {
                    if (it.hasFocus) {
                        driverExpanded.value = !driverExpanded.value
                    }
                }
            )

            if (driverExpanded.value) {
                DropdownMenu(
                    expanded = driverExpanded.value,
                    modifier = Modifier.requiredWidthIn(min = 64.dp),
                    onDismissRequest = { driverExpanded.value = false }
                ) {
                    driverState.value.forEach {
                        DropdownMenuItem(
                            onClick = {
                                driverExpanded.value = false
                                useCase.selectDriver(it)
                            }
                        ) {
                            Text(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FootageReaderBar(useCase: DashCamFileUseCase) {
    val footageDirs = useCase.footageDirStateFlow.collectAsState(emptyList())

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.requiredWidth(buttonWidth.dp),
            onClick = {}) {
            Text("Read")
        }

        OutlinedTextField(
            value = footageDirs.value.firstOrNull()?.canonicalPath ?: "",
            onValueChange = { },
            label = { Text("Front") }
        )
        OutlinedTextField(
            value = if(footageDirs.value.size>1) {
                footageDirs.value[1].canonicalPath
            } else "",
            onValueChange = { },
            label = { Text("Rear") }
        )
    }
}

@Composable
fun OutputCopyBar(useCase: DashCamFileUseCase) {
    val outputPath = remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.requiredWidth(buttonWidth.dp),
            onClick = {}) {
            Text("Save")
        }

        Row(modifier = Modifier.wrapContentHeight()) {
            OutlinedTextField(
                value = outputPath.value,
                onValueChange = { outputPath.value = it },
                label = { Text("Output") }
            )
        }
    }
}