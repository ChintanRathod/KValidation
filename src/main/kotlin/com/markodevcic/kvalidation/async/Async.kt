package com.markodevcic.kvalidation.async

import java.util.concurrent.Executor
import java.util.concurrent.Executors


inline fun <T, TR> T.doAsync(crossinline valueFactory: () -> TR,
                             crossinline callback: (TR?, Exception?) -> Unit,
                             workExecutor: Executor = Executors.newSingleThreadExecutor(),
                             callbackExecutor: Executor? = null) {
    return workExecutor.execute {
        var result: TR? = null
        try {
            result = valueFactory()
        } catch (e: Exception) {
            if (callbackExecutor != null) {
                callbackExecutor.execute { callback(null, e) }
            } else {
                callback(null, e)
            }
        }
        if (result != null) {
            if (callbackExecutor != null) {
                callbackExecutor.execute { callback(result, null) }
            } else {
                callback(result, null)
            }
        }
    }
}