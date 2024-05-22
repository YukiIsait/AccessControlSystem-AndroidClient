package tech.youko.smartcard.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.youko.smartcard.R
import tech.youko.smartcard.ui.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel()
) {
//    val server by viewModel.server.collectAsState()
    val id by viewModel.id.collectAsState()
    val password by viewModel.password.collectAsState()

    val state by viewModel.state.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()

    when (state) {
        LoginViewModel.UIState.ERROR -> {
            AlertDialog(
                onDismissRequest = viewModel::switchToIdle,
                title = {
                    Text(stringResource(R.string.title_error))
                },
                text = {
                    Text(errorMessage)
                },
                confirmButton = {
                    Button(
                        onClick = viewModel::switchToIdle
                    ) {
                        Text(stringResource(R.string.title_ok))
                    }
                }
            )
        }

        LoginViewModel.UIState.LOADING -> {
            AlertDialog(
                onDismissRequest = viewModel::switchToIdle,
                title = {
                    Text(stringResource(R.string.title_loading))
                },
                text = {
                    Text(stringResource(R.string.message_please_wait_for_login))
                },
                confirmButton = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
        }

        else -> {}
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.title_app_name))
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(40.dp)
        ) {
            Text(
                text = stringResource(R.string.title_welcome),
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.title_login),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.height(16.dp))
//        TextField(
//            value = server,
//            onValueChange = viewModel::updateServer,
//            singleLine = true,
//            label = {
//                Text(stringResource(R.string.title_server))
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(Modifier.height(16.dp))
            TextField(
                value = id,
                onValueChange = viewModel::updateId,
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_id))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = viewModel::updatePassword,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                label = {
                    Text(stringResource(R.string.title_password))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = viewModel::onLogin
                ) {
                    Text(stringResource(R.string.title_login))
                }
            }
        }
        Column(
            Modifier
                .padding(innerPadding)
                .padding(40.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(stringResource(R.string.message_copyright))
        }
    }
}
