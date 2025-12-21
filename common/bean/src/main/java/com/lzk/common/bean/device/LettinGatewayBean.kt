package com.lzk.common.bean.device

import com.google.gson.annotations.SerializedName

data class HqBean(
    @SerializedName("Mac")
    val mac: String? = null,
    @SerializedName("Obj")
    val obj: HqObj? = null,
)

data class HqObj(
    @SerializedName("Name")
    val name: String? = null,
)
