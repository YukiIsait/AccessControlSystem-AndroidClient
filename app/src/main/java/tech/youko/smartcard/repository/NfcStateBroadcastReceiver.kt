package tech.youko.smartcard.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NfcStateBroadcastReceiver : BroadcastReceiver() {

    private val filter = IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)

    private val _nfcState = MutableSharedFlow<Int>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val nfcState = _nfcState.asSharedFlow()

    override fun onReceive(context: Context, intent: Intent) {
        _nfcState.tryEmit(intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, NfcAdapter.STATE_OFF))
    }

    fun register(context: Context) {
        context.registerReceiver(this, filter)
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(this)
    }
}
