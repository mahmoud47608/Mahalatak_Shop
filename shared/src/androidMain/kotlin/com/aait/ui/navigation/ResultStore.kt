package com.aait.helool.util.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

@Stable
class ResultStore {
    private val results = mutableMapOf<Any, Any?>()

    fun <T> setResult(key: Any, t: T) {
        results[key] = t
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getResult(key: Any): T? = results[key] as? T

    @Suppress("UNCHECKED_CAST")
    fun removeResult(key: Any) = results.remove(key)

    companion object {
        val saver = Saver<ResultStore, Map<Any, Any?>>(
            save = { it.results.toMap() },
            restore = {
                ResultStore().apply {
                    results.putAll(it)
                }
            }
        )
    }
}

@Composable
fun rememberResultStore() = rememberSaveable(saver = ResultStore.saver) {
    ResultStore()
}

enum class ResultKeys {
    LOCATION,
    UPDATE
}