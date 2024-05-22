package tech.youko.smartcard.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.youko.smartcard.R
import tech.youko.smartcard.ui.component.ErrorComponent
import tech.youko.smartcard.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        MainViewModel.UIPage.HOME -> {
            HomeScreen()
        }

        MainViewModel.UIPage.LOGIN -> {
            LoginScreen()
        }

        MainViewModel.UIPage.MAINTENANCE -> {
            MaintenanceScreen()
        }

        MainViewModel.UIPage.ERROR -> {
            ErrorComponent(
                onExit = viewModel::onLogout,
                message = stringResource(R.string.message_invalid_login_information)
            )
        }
    }
}
