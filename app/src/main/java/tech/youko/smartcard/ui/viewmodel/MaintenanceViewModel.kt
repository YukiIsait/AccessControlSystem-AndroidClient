package tech.youko.smartcard.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.youko.smartcard.R
import tech.youko.smartcard.entity.DeviceInformation
import tech.youko.smartcard.repository.GlobalState

class MaintenanceViewModel(application: Application) : AndroidViewModel(application) {
    private var _mac = MutableStateFlow("")
    val mac = _mac.asStateFlow()
    private var _ip = MutableStateFlow("")
    val ip = _ip.asStateFlow()
    private var _subnet = MutableStateFlow("")
    val subnet = _subnet.asStateFlow()
    private var _gateway = MutableStateFlow("")
    val gateway = _gateway.asStateFlow()
    private var _dns = MutableStateFlow("")
    val dns = _dns.asStateFlow()
    private var _server = MutableStateFlow(
        getApplication<Application>().getString(R.string.address_default_server_ip)
    )
    val server = _server.asStateFlow()
    private var _port = MutableStateFlow(
        getApplication<Application>().getString(R.string.address_default_server_port).toInt()
    )
    val port = _port.asStateFlow()
    private var _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    fun updateId(value: String) {
        _id.value = value
    }

    fun updateMac(value: String) {
        _mac.value = value
    }

    fun updateIp(value: String) {
        _ip.value = value
    }

    fun updateSubnet(value: String) {
        _subnet.value = value
    }

    fun updateGateway(value: String) {
        _gateway.value = value
    }

    fun updateDns(value: String) {
        _dns.value = value
    }

    fun updateServer(value: String) {
        _server.value = value
    }

    fun updatePort(value: Int) {
        _port.value = value
    }

    fun onEndMaintenance() {
        GlobalState.updateMaintenanceState(false)
    }

    fun saveDeviceInformation() {
        GlobalState.updateDeviceInformation(
            DeviceInformation(
                mac.value,
                ip.value,
                subnet.value,
                gateway.value,
                dns.value,
                server.value,
                port.value,
                id.value
            )
        )
        GlobalState.updateMaintenanceState(false)
    }
}
