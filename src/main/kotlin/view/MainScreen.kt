package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import domain.DashCamFileUseCase

@Composable
fun MainScreen(
    useCase: DashCamFileUseCase = DashCamFileUseCase()
) {
    val uiState = useCase.stateFlow.collectAsState(null).value

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.wrapContentHeight()) {
            Button(onClick = {}) {
                Text("Read")
            }
        }

        Row(modifier = Modifier.wrapContentHeight()) {
            Text("Path")
        }

        Row(modifier = Modifier.weight(8.0f)) {
            Column {
                uiState?.forEach {
                    Row {
                        Text(it.name)
                    }
                }
            }
        }

        Row(modifier = Modifier.wrapContentHeight()) {
            Text("Status bar")
        }
    }
}