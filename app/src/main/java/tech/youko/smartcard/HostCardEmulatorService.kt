package tech.youko.smartcard

import android.content.Intent
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import tech.youko.smartcard.repository.GlobalState
import tech.youko.smartcard.util.asHexStringToByteArray

class HostCardEmulatorService : HostApduService() {
    companion object {
        val STATUS_SUCCESS = byteArrayOf(0x90.toByte(), 0x00.toByte())
        val STATUS_FAILED = byteArrayOf(0x6F.toByte(), 0x00.toByte())
        val CLA_NOT_SUPPORTED = byteArrayOf(0x6E.toByte(), 0x00.toByte())
        val INS_NOT_SUPPORTED = byteArrayOf(0x6D.toByte(), 0x00.toByte())
        val INVALID_PARAMETER = byteArrayOf(0x6B.toByte(), 0x00.toByte())
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu?.isEmpty() != false || commandApdu.size < 12) {
            return STATUS_FAILED
        }

        if (commandApdu[0] != 0x00.toByte()) {
            return CLA_NOT_SUPPORTED
        }
        if (commandApdu[1] != 0xA4.toByte()) {
            return INS_NOT_SUPPORTED
        }
        if (commandApdu[2] != 0x04.toByte() ||
            commandApdu[3] != 0x00.toByte()
        ) {
            return INVALID_PARAMETER
        }

        startActivity(
            Intent(
                this,
                MainActivity::class.java
            ).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )

        val loginInformation = GlobalState.loginInformation.value ?: return STATUS_FAILED
        val guardAid = getString(R.string.aid_guard)
        if (commandApdu[4] == guardAid.length.ushr(1).toByte() && // 字节长度等于AID字符串长度的一半
            commandApdu.slice(5 until 5 + commandApdu[4]) // 从第5个字节开始取长度为第4个字节的数据
                .toByteArray()
                .contentEquals(guardAid.asHexStringToByteArray()) // 判断是否等于AID字符串的字节数据
        ) {
            GlobalState.updateCommunicationState(true)
            val binary = loginInformation.information.id.toByteArray()
            return byteArrayOf(binary.size.toByte()) + binary + STATUS_SUCCESS
        }

        val deviceInformation = GlobalState.deviceInformation.value ?: return STATUS_FAILED
        val maintenanceAid = getString(R.string.aid_maintenance)
        if (commandApdu[4] == maintenanceAid.length.ushr(1).toByte() &&
            commandApdu.slice(5 until 5 + commandApdu[4])
                .toByteArray()
                .contentEquals(maintenanceAid.asHexStringToByteArray())
        ) {
            val binary = deviceInformation.toBinary()
            if (binary.isEmpty()) {
                return STATUS_FAILED
            }
            GlobalState.updateCommunicationState(true)
            return deviceInformation.toBinary() + STATUS_SUCCESS
        }

        return STATUS_FAILED
    }

    override fun onDeactivated(reason: Int) {}
}
