package com.lzk.core.network

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun String.toJsonRequestBody(): RequestBody = toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
