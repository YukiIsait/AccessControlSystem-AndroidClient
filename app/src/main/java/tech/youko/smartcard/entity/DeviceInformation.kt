package tech.youko.smartcard.entity

import java.io.Serializable

data class DeviceInformation(
    val mac: String,
    val ip: String,
    val subnet: String,
    val gateway: String,
    val dns: String,
    val server: String,
    val port: Int,
    val id: String
) : Serializable
{
    fun toBinary(): ByteArray {
        return try {
            val mac = mac.split(":").map { it.toInt(16).toByte() }.toByteArray()
            val ip = ip.split(".").map { it.toInt().toByte() }.toByteArray()
            val subnet = subnet.split(".").map { it.toInt().toByte() }.toByteArray()
            val gateway = gateway.split(".").map { it.toInt().toByte() }.toByteArray()
            val dns = dns.split(".").map { it.toInt().toByte() }.toByteArray()
            val server = server.split(".").map { it.toInt().toByte() }.toByteArray()
            val port = byteArrayOf(port.and(0xFF).toByte(), port.shr(8).toByte())
            val id = id.toByteArray()
            byteArrayOf(*mac, *ip, *subnet, *gateway, *dns, *server, *port, id.size.toByte(), *id)
        } catch (e: Exception) {
            byteArrayOf()
        }
    }
}
