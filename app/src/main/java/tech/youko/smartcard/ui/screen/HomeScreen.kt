package tech.youko.smartcard.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.youko.smartcard.R
import tech.youko.smartcard.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState(HomeViewModel.UIState.NORMAL)
    val showMaintenance by viewModel.showMaintenance.collectAsState(false)

    val id by viewModel.id.collectAsState()
    val name by viewModel.name.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.title_app_name))
                },
                actions = {
                    IconButton(
                        onClick = viewModel::onLogout
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_logout),
                            contentDescription = stringResource(R.string.title_logout)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (showMaintenance) {
                ExtendedFloatingActionButton(
                    onClick = viewModel::onMaintain,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_maintenance),
                            contentDescription = stringResource(R.string.title_maintain)
                        )
                    },
                    text = {
                        Text(stringResource(R.string.title_maintain))
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(40.dp)
                .fillMaxWidth()
                .wrapContentWidth()
        ) {
            Row {
                Icon(
                    painter = painterResource(R.drawable.icon_id),
                    contentDescription = stringResource(R.string.title_id)
                )
                Spacer(Modifier.width(8.dp))
                Text(id)
            }
            Spacer(Modifier.height(8.dp))
            Row {
                Icon(
                    painter = painterResource(R.drawable.icon_user),
                    contentDescription = stringResource(R.string.title_name)
                )
                Spacer(Modifier.width(8.dp))
                Text(name)
            }
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(
                    when (state) {
                        HomeViewModel.UIState.NORMAL -> R.drawable.icon_nfc
                        HomeViewModel.UIState.NORMAL_OK -> R.drawable.icon_ok
                        else -> R.drawable.icon_no_nfc
                    }
                ),
                contentDescription = stringResource(R.string.title_contact),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(128.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(
                    when (state) {
                        HomeViewModel.UIState.UNSUPPORTED -> R.string.message_device_does_not_support_nfc
                        HomeViewModel.UIState.DISABLED -> R.string.message_nfc_is_disabled
                        HomeViewModel.UIState.NORMAL_OK -> R.string.message_communication_complete
                        else -> R.string.message_close_to_the_card_reader
                    }
                ),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = viewModel::onOpenNfcSettings
            ) {
                Text(stringResource(R.string.title_open_nfc_settings))
            }
        }
    }
}
