@file:Suppress("ktlint:standard:filename")

package com.lzk.core.utils

object Utils {
    /**
     * bytes转16进制字符串
     *
     * @param src
     * @return
     */
    fun bytesToHexString(src: ByteArray?): String? {
        val stringBuilder = StringBuilder("")
        if (src == null || src.isEmpty()) {
            return null
        }
        for (i in src.indices) {
            val v = src[i].toInt() and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
        }
        return stringBuilder.toString()
    }

    fun hlvalue2decimal(
        bh: Byte,
        bl: Byte,
    ): Int = (bh.toInt() and 0xff) * 256 + (bl.toInt() and 0xff)

    @Throws(Exception::class)
    fun parasUdpJson(data: ByteArray): String {
        if (data.size > 24) {
            val jsonLength: Int = hlvalue2decimal(data[22], data[23])
            val jsonBytes = ByteArray(jsonLength)
            System.arraycopy(data, 24, jsonBytes, 0, jsonLength)
            return String(jsonBytes, charset("UTF-8"))
        }
        return ""
    }

    fun short2ByteArray(value: Short): ByteArray {
        val data = ByteArray(2)
        if (value > 0xff) {
            data[1] = value.toByte()
            data[0] = (value.toInt() ushr 8).toByte()
        } else {
            data[1] = value.toByte()
            data[0] = 0x00
        }
        return data
    }

    fun stringToByteAddress(address: String): ByteArray {
        var arr: Array<String> =
            address.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val addr = ByteArray(8)
        for (i in 0..7) {
            addr[i] = arr[i].toInt(16).toByte()
        }
        return addr
    }

    fun short2bytearray(value: Int): ByteArray = short2bytearray(value, false)

    fun short2bytearray(
        value: Int,
        isLittleEndian: Boolean,
    ): ByteArray {
        val bytes = ByteArray(2)
        if (isLittleEndian) {
            bytes[0] = (value and 0xFF).toByte()
            bytes[1] = (((value and 0xFFFF) shr 8) and 0xFF).toByte()
        } else {
            bytes[1] = (value and 0xFF).toByte()
            bytes[0] = (((value and 0xFFFF) shr 8) and 0xFF).toByte()
        }
        return bytes
    }

    fun crc16Verify_impl(data: ByteArray): Int {
        var crcWord = 0x0000ffff
        for (i in data.indices) {
            crcWord = crcWord xor (data[i].toInt() and 0x000000ff)
            for (j in 0..7) {
                if ((crcWord and 0x00000001) == 1) {
                    crcWord = crcWord shr 1
                    crcWord = crcWord xor 0x0000A001
                } else {
                    crcWord = (crcWord shr 1)
                }
            }
        }
        return crcWord
    }

    fun crc16VerifyGetShort(data: ByteArray): Short {
        val crcWord: Int = crc16Verify_impl(data)
        val crcValue = (crcWord and 0x0000FFFF).toShort()
        return crcValue
    }

    fun crc16VerifyGetByte(
        data: ByteArray,
        little_endian: Boolean = false,
    ): ByteArray {
        val value = ByteArray(2)
        val crcValue: Short = crc16VerifyGetShort(data)
        if (little_endian) {
            value[0] = (crcValue.toInt() and 0x000000ff).toByte()
            value[1] = ((crcValue.toInt() shr 8) and 0x000000ff).toByte()
        } else {
            value[1] = (crcValue.toInt() and 0x000000ff).toByte()
            value[0] = ((crcValue.toInt() shr 8) and 0x000000ff).toByte()
        }
        return value
    }


}
