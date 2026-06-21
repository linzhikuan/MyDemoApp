package com.lzk.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

fun launch(block: suspend () -> Unit) {
    ThreadExecutor.launch {
        block.invoke()
    }
}

fun launchMain(block: () -> Unit) {
    ThreadExecutor.runMain {
        block.invoke()
    }
}

fun runThread(block: () -> Unit) {
    ThreadExecutor.runThread {
        block.invoke()
    }
}

object ThreadExecutor {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun launch(block: suspend () -> Unit) =
        scope.launch {
            block.invoke()
        }

    fun runThread(block: () -> Unit) {
        scope.launch {
            block
        }
    }

    fun runMain(block: () -> Unit) {
        scope.launch(Dispatchers.Main) {
            block()
        }
    }
}
