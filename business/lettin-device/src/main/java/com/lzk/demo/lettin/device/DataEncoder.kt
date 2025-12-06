package com.lzk.demo.lettin.device

import com.lzk.core.utils.Utils


object DataEncoder {
    fun hqDataEncode(
        data: ByteArray,
        cmd: Int,
        tid: Int,
    ): List<ByteArray> {
        val chunkSize = 450
        val totalSize = data.size
        val first = totalSize / chunkSize
        val last = totalSize % chunkSize
        val list = arrayListOf<ByteArray>()
        val totalPackage = first + if (last > 0) 1 else 0
        for (index in 0 until first) {
            val start = index * chunkSize
            val end = start + chunkSize
            val currentData = data.copyOfRange(start, end)
            list.add(hqDataEncode(currentData, cmd, tid, totalPackage, index))
        }
        if (last > 0) {
            val start = first * chunkSize
            val end = start + last
            val currentData = data.copyOfRange(start, end)
            list.add(hqDataEncode(currentData, cmd, tid, totalPackage, first))
        }
        return list
    }

    fun hqDataEncode(
        data: ByteArray,
        cmd: Int,
        tid: Int,
        total: Int,
        index: Int,
    ): ByteArray {
        val tag = Utils.short2bytearray(cmd) // 2个字节的cmd编码
        val identity = Utils.short2bytearray(tid) // 2个字节的随机标识
        val totalPakageByts = Utils.short2bytearray(total) // 2个字节的数据总包数
        val indexByts = Utils.short2bytearray(index) // 2个字节的当前包的索引
        val nowPakageLenth = Utils.short2bytearray(data.size) // 2个字节的当前包的数据长度
        val parmsByts =
            ByteArray(tag.size + identity.size + totalPakageByts.size + indexByts.size + nowPakageLenth.size + data.size)
        System.arraycopy(tag, 0, parmsByts, 0, tag.size)
        System.arraycopy(identity, 0, parmsByts, tag.size, identity.size)
        System.arraycopy(
            totalPakageByts,
            0,
            parmsByts,
            tag.size + identity.size,
            totalPakageByts.size,
        )
        System.arraycopy(
            indexByts,
            0,
            parmsByts,
            tag.size + identity.size + totalPakageByts.size,
            indexByts.size,
        )
        System.arraycopy(
            nowPakageLenth,
            0,
            parmsByts,
            tag.size + identity.size + totalPakageByts.size + indexByts.size,
            nowPakageLenth.size,
        )
        System.arraycopy(
            data,
            0,
            parmsByts,
            tag.size + identity.size + totalPakageByts.size + indexByts.size + nowPakageLenth.size,
            data.size,
        )
        return LettinCmdBytesArray_Impl(parmsByts)
    }

    private fun LettinCmdBytesArray_Impl(param: ByteArray?): ByteArray {
        val cmdLenSize = 2
        var totalSendLen = 0
        val byteCmdLen: ByteArray
        val tempCmd: ByteArray
        val crcByteValue: ByteArray
        val sendCmd: ByteArray

        val cmdCode = Utils.short2bytearray(0x0410)
        val dstmac = Utils.stringToByteAddress("FF:FF:FF:FF:FF:FF:FF:FF")

        if (param != null) {
            totalSendLen = dstmac.size + cmdCode.size + param.size
            tempCmd = ByteArray(totalSendLen)
            System.arraycopy(dstmac, 0, tempCmd, 0, dstmac.size)
            System.arraycopy(cmdCode, 0, tempCmd, dstmac.size, cmdCode.size)
            System.arraycopy(param, 0, tempCmd, dstmac.size + cmdCode.size, param.size)
        } else {
            totalSendLen = dstmac.size + cmdCode.size
            tempCmd = ByteArray(totalSendLen)
            System.arraycopy(dstmac, 0, tempCmd, 0, dstmac.size)
            System.arraycopy(cmdCode, 0, tempCmd, dstmac.size, cmdCode.size)
            //            LettinUtils.MyLogf("tempCmd.length: %d\n",tempCmd.length);
        }
        // tempCmd数据是用作crc校验的数据
        crcByteValue = Utils.crc16VerifyGetByte(tempCmd)
        // 总发送长度为指令长度+crc校验段指令+crc校验字节长度
        totalSendLen = tempCmd.size + cmdLenSize + crcByteValue.size
        byteCmdLen = ByteArray(cmdLenSize)
        for (i in 0 until cmdLenSize) {
            byteCmdLen[cmdLenSize - 1 - i] = (totalSendLen shr 8 * i and 0xFF).toByte()
        }
        sendCmd = ByteArray(totalSendLen)
        //        LettinUtils.MyLogf("tempCmd.length: %d,totalSendLen:%d\n",tempCmd.length,totalSendLen);
        System.arraycopy(byteCmdLen, 0, sendCmd, 0, byteCmdLen.size)
        System.arraycopy(tempCmd, 0, sendCmd, byteCmdLen.size, tempCmd.size)
        System.arraycopy(
            crcByteValue,
            0,
            sendCmd,
            byteCmdLen.size + tempCmd.size,
            crcByteValue.size,
        )
        return sendCmd
    }
}
