package com.lzk.core.utils

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.ref.WeakReference

fun getApp(): Application = AppUtil.getApp()

object AppUtil {
    private lateinit var app: Application
    private val _crashState: MutableStateFlow<Throwable?> = MutableStateFlow(null)
    val crashState: StateFlow<Throwable?> get() = _crashState
    private val _isAppInForeground: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAppInForeground: StateFlow<Boolean> get() = _isAppInForeground
    private val visibleActivities = linkedSetOf<WeakReference<Activity>>()

    fun init(application: Application) {
        app = application
        observeAppLifecycle()
        observeCrashHandler()
    }

    fun getApp(): Application = app

    private fun observeCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                _crashState.value = throwable
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }
    }

    private fun observeAppLifecycle() {
        app.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?,
                ) {
                }

                override fun onActivityStarted(activity: Activity) {}

                override fun onActivityResumed(activity: Activity) {
                    visibleActivities.add(WeakReference(activity))
                    _isAppInForeground.value = visibleActivities.isNotEmpty()
                }

                override fun onActivityPaused(activity: Activity) {}

                override fun onActivityStopped(activity: Activity) {
                    visibleActivities.removeIf { it.get() === activity }
                    _isAppInForeground.value = visibleActivities.isNotEmpty()
                }

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle,
                ) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    visibleActivities.removeIf { it.get() === activity }
                    _isAppInForeground.value = visibleActivities.isNotEmpty()
                }
            },
        )
    }
}
