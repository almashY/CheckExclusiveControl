package com.example.checkexclusivecontrol

import android.content.Context

class APIWrapper(private val context: Context) {

    private var cachedItems: List<Item>? = null

    fun fetchAllItemsRecursively(
        offset: Int = 0,
        limit: Int = 1000,
        maxCount: Int = 6000,
        accumulator: MutableList<Item> = mutableListOf(),
        onSuccess: (List<Item>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        fetchFromServer(
            offset = offset,
            limit = limit,
            onSuccess = { items ->
                accumulator.addAll(items)

                if (accumulator.size >= maxCount || items.isEmpty()) {
                    onSuccess(accumulator)
                    return@fetchFromServer
                }

                fetchAllItemsRecursively(
                    offset = offset + limit,
                    limit = limit,
                    maxCount = maxCount,
                    accumulator = accumulator,
                    onSuccess = onSuccess,
                    onError = onError
                )
            },
            onError = { e ->
                onError(e)
            }
        )
    }

    fun fetchFromServer(
        offset: Int,
        limit: Int,
        onSuccess: (List<Item>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            if (cachedItems == null) {
                val json = context.assets.open("users_less_data.json")
                    .bufferedReader()
                    .use { it.readText() }

                val type = object : com.google.gson.reflect.TypeToken<List<Item>>() {}.type
                cachedItems = com.google.gson.Gson().fromJson(json, type)
            }

            val allItems = cachedItems ?: emptyList()

            val sliced = allItems
                .drop(offset)
                .take(limit)

            onSuccess(sliced)

        } catch (e: Exception) {
            onError(e)
        }
    }

}

data class Item(
    val id: Int,
    val name: String,
    val email: String
)



