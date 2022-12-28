package com.pactsafe.pactsafeandroidsdk.data

import com.pactsafe.pactsafeandroidsdk.PSLogger


fun handleThrowable(throwable: Throwable) {
    PSLogger.errorLog("PSLogger", throwable, throwable.localizedMessage)
}