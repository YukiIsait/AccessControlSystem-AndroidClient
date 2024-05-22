package tech.youko.smartcard.util

private const val HEX_CHARS = "0123456789ABCDEF"

fun String.asHexStringToByteArray(): ByteArray =
    ByteArray(this.length.ushr(1)) {
        val index = it.shl(1)
        HEX_CHARS.indexOf(this[index])
            .shl(4)
            .or(HEX_CHARS.indexOf(this[index.plus(1)]))
            .toByte()
    }

//fun String.asDelimitedHexStringToByteArray(): ByteArray =
//    ByteArray(this.length.plus(1).div(3)) {
//        val index = it.shl(1).plus(it)
//        HEX_CHARS.indexOf(this[index])
//            .shl(4)
//            .or(HEX_CHARS.indexOf(this[index.plus(1)]))
//            .toByte()
//    }

fun ByteArray.toHexString(appendable: Appendable = StringBuilder()): String =
    this.fold(appendable) { result, byte ->
        val octet = byte.toInt()
        result.append(HEX_CHARS[octet.ushr(4)]).append(HEX_CHARS[octet.and(0x0F)])
    }.toString()

//fun ByteArray.toHexString(delimiter: Char, appendable: Appendable = StringBuilder()): String {
//    val lastIndex = this.size.minus(1)
//    return this.foldIndexed(appendable) { index, result, byte ->
//        val octet = byte.toInt()
//        result.append(HEX_CHARS[octet.ushr(4)]).append(HEX_CHARS[octet.and(0x0F)])
//        if (index < lastIndex) {
//            result.append(delimiter)
//        } else {
//            result
//        }
//    }.toString()
//}
