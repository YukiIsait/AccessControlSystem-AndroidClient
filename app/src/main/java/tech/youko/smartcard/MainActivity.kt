package tech.youko.smartcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import tech.youko.smartcard.network.NetworkClient
import tech.youko.smartcard.repository.GlobalState
import tech.youko.smartcard.repository.NfcStateBroadcastReceiver
import tech.youko.smartcard.ui.screen.MainScreen
import tech.youko.smartcard.ui.theme.SmartCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NfcStateBroadcastReceiver.register(this)
        NetworkClient.initialize(this)
        GlobalState.initialize(this, lifecycleScope)
        GlobalState.initialize(this, lifecycleScope)
        setContent {
            SmartCardTheme(dynamicColor = false) {
                Surface(Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NfcStateBroadcastReceiver.unregister(this)
    }
}
