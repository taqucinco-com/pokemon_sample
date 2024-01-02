package com.taqucinco.pokemon_sample.ui.route.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.taqucinco.pokemon_sample.ui.component.TopAppBar
import com.taqucinco.pokemon_sample.ui.hooks.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    scaffoldPadding: PaddingValues? = null,
    appState: AppState,
    onNavigate: (String) -> Unit = { },
    viewModel: SettingsViewModel = hiltViewModel(),
) {

//    var text by remember { mutableStateOf("テキスト") }
//    var saveableText by rememberSaveable { mutableStateOf("rememberSaveableのテキスト") }
//    val saveableState = rememberSomeSaveableState() // このstateはタブ移動やpush/popなどで画面を一度破棄しても復元される

    DisposableEffect(Unit) {
        print(viewModel.getValue)
        onDispose {
            viewModel.increment()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                title = "設定"
            )
        },
        modifier = if (scaffoldPadding != null) Modifier.padding(scaffoldPadding) else Modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ListItem("利用規約", onClick = { onNavigate("/term-of-service") })

//                TextField(
//                    value = text,
//                    onValueChange = { text = it }
//                )
//
//                TextField(
//                    value = saveableText,
//                    onValueChange = { saveableText = it }
//                )
            }
        }
    }
}

@Composable
private fun ListItem(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text)
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
