package tech.youko.smartcard.repository

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.youko.smartcard.R
import tech.youko.smartcard.entity.DeviceInformation
import tech.youko.smartcard.entity.LoginInformation
import tech.youko.smartcard.util.initializeStorage

object GlobalState {
    private val _loginInformation = MutableStateFlow<LoginInformation?>(null)
    val loginInformation = _loginInformation.asStateFlow()

    private val _deviceInformation = MutableStateFlow<DeviceInformation?>(null)
    val deviceInformation = _deviceInformation.asStateFlow()

    private val _inMaintenance = MutableStateFlow(false)
    val inMaintenance = _inMaintenance.asStateFlow()

    private val _communicationComplete = MutableStateFlow(false)
    val communicationComplete = _communicationComplete.asStateFlow()

    fun updateLoginInformation(loginInformation: LoginInformation?) {
        _loginInformation.value = loginInformation
    }

    fun updateDeviceInformation(deviceInformation: DeviceInformation?) {
        _deviceInformation.value = deviceInformation
    }

    fun updateMaintenanceState(maintenanceState: Boolean) {
        _inMaintenance.value = maintenanceState
    }

    fun updateCommunicationState(communicationState: Boolean) {
        _communicationComplete.value = communicationState
    }

    fun initialize(context: Context, coroutineScope: CoroutineScope) {
        _loginInformation.initializeStorage(
            coroutineScope,
            context,
            context.getString(R.string.file_login_information)
        )
        coroutineScope.launch {
            _loginInformation.collect {
                if (it == null) {
                    updateMaintenanceState(false)
                }
            }
        }
        coroutineScope.launch {
            _communicationComplete.collect {
                if (it) {
                    delay(10000)
                    updateCommunicationState(false)
                }
            }
        }
    }
}
