package com.istudio.crsurfguide.ui.debug

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LogBuffer {
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs: StateFlow<List<String>> = _logs.asStateFlow()
    
    private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    fun d(tag: String, message: String) {
        Log.d(tag, message)
        addLog("DEBUG", tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
        val msg = if (throwable != null) "$message \n${throwable.stackTraceToString()}" else message
        addLog("ERROR", tag, msg)
    }

    @Synchronized
    private fun addLog(level: String, tag: String, message: String) {
        val time = dateFormat.format(Date())
        val formattedLog = "[$time] $level/$tag: $message"
        val currentList = _logs.value.toMutableList()
        if (currentList.size >= 150) {
            currentList.removeAt(0)
        }
        currentList.add(formattedLog)
        _logs.value = currentList
    }
}
