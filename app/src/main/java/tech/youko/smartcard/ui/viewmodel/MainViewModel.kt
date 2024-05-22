package tech.youko.smartcard.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.youko.smartcard.network.NetworkClient
import tech.youko.smartcard.repository.GlobalState

class MainViewModel(application: Application) : AndroidViewModel(application) {
    enum class UIPage {
        HOME,
        LOGIN,
        MAINTENANCE,
        ERROR
    }

    private val _state = MutableStateFlow(UIPage.HOME)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            GlobalState.loginInformation.collect {
                if (it == null) {
                    _state.value = UIPage.LOGIN
                } else {
                    try {
                        if (NetworkClient.api.validate(it.token).data!!) {
                            _state.value = UIPage.HOME
                        } else {
                            _state.value = UIPage.ERROR
                        }
                    } catch (e: Exception) {
                        _state.value = UIPage.ERROR
                    }
                }
            }
        }
        viewModelScope.launch {
            GlobalState.inMaintenance.collect {
                if (GlobalState.loginInformation.value != null) {
                    if (it) {
                        _state.value = UIPage.MAINTENANCE
                    } else {
                        _state.value = UIPage.HOME
                    }
                }
            }
        }
        viewModelScope.launch {
            GlobalState.communicationComplete.collect {
                if (it) {
                    _state.value = UIPage.HOME
                }
            }
        }
    }

    fun onLogout() {
        GlobalState.updateLoginInformation(null)
    }
}
