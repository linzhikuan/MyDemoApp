package com.lzk.demo.lettin.device.inner

interface LettinGwHelper {
    suspend fun syncTable(
        ip: String,
        gwMac: String,
    )
}
