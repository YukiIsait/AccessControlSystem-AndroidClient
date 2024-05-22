package tech.youko.smartcard.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import tech.youko.smartcard.R
import tech.youko.smartcard.repository.GlobalState
import tech.youko.smartcard.repository.NfcStateBroadcastReceiver

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    enum class UIState {
        NORMAL,
        NORMAL_OK,
        DISABLED,
        UNSUPPORTED
    }

    val state = NfcStateBroadcastReceiver.nfcState.map {
        when (it) {
            NfcAdapter.STATE_ON -> UIState.NORMAL
            else -> UIState.DISABLED
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = getNfcState()
    ).combine(GlobalState.communicationComplete) { nfcState, communicationComplete ->
        if (communicationComplete) {
            UIState.NORMAL_OK
        } else {
            nfcState
        }
    }

    val showMaintenance = combine(
        GlobalState.loginInformation,
        state
    ) { loginInformation, state ->
        state != UIState.DISABLED && state != UIState.UNSUPPORTED &&
                loginInformation?.information?.authorities?.contains(
                    getApplication<Application>().getString(R.string.role_maintainer)
                ) ?: false
    }

    val id = GlobalState.loginInformation.map {
        it?.information?.id ?: ""
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GlobalState.loginInformation.value?.information?.id ?: ""
    )

    val name = GlobalState.loginInformation.map {
        it?.information?.name ?: ""
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GlobalState.loginInformation.value?.information?.name ?: ""
    )

    private fun getNfcState(): UIState {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(getApplication())
        return when {
            nfcAdapter == null -> UIState.UNSUPPORTED
            nfcAdapter.isEnabled -> UIState.NORMAL
            else -> UIState.DISABLED
        }
    }

    fun onOpenNfcSettings() {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(intent)
    }

    fun onMaintain() {
        GlobalState.updateMaintenanceState(true)
    }

    fun onLogout() {
        GlobalState.updateLoginInformation(null)
    }
}
