package tech.youko.smartcard.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.youko.smartcard.R
import tech.youko.smartcard.entity.LoginInformation
import tech.youko.smartcard.network.NetworkClient
import tech.youko.smartcard.repository.GlobalState

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    enum class UIState {
        IDLE,
        LOADING,
        SUCCESS,
        ERROR
    }

//    private val _server = MutableStateFlow(
//        getApplication<Application>().getString(R.string.address_default_server)
//    )
//    val server = _server.asStateFlow()

    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _state = MutableStateFlow(UIState.IDLE)
    val state = _state.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private fun setLoginData(loginData: LoginInformation) {
        this._state.value = UIState.SUCCESS
        GlobalState.updateLoginInformation(loginData)
    }

    private fun setErrorMessage(errorMessage: String) {
        this._state.value = UIState.ERROR
        this._errorMessage.value = errorMessage
    }

//    fun updateServer(server: String) {
//        this._server.value = server
//    }

    fun updateId(id: String) {
        this._id.value = id
    }

    fun updatePassword(password: String) {
        this._password.value = password
    }

    fun switchToIdle() {
        this._state.value = UIState.IDLE
    }

    fun onLogin() {
//        if (server.value.isNotEmpty()) {
//            NetworkClient.server = server.value
//        }
        if (id.value.isEmpty() || password.value.isEmpty()) {
            setErrorMessage(
                getApplication<Application>().getString(R.string.message_id_or_password_is_empty)
            )
            return
        }
        _state.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val loginResult = NetworkClient.api.login(id.value, password.value)
                if (loginResult.isSuccessful) {
                    setLoginData(loginResult.data!!)
                } else {
                    setErrorMessage(loginResult.message)
                }
            } catch (e: Exception) {
                setErrorMessage(
                    e.message ?: getApplication<Application>()
                        .getString(R.string.message_unknown_error)
                )
            }
        }
    }
}
