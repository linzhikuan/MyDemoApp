package com.lzk.common.bean.device

import com.google.gson.annotations.SerializedName

data class HqBean(
    @SerializedName("Mac")
    val mac: String? = null,
    @SerializedName("Obj")
    val obj: HqObj? = null,
    @SerializedName("data")
    val data: HqData? = null,
)

data class HqObj(
    @SerializedName("Name")
    val name: String? = null,
)

data class HqData(
    @SerializedName("Name")
    val name: String? = null,
)
