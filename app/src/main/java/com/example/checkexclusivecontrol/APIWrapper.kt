package com.example.checkexclusivecontrol

import android.content.Context
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class APIWrapper(private val context: Context) {

    private var cachedItems: List<MenuItemsDatabaseData>? = null

    fun fetchAllItemsRecursively(
        offset: Int = 0,
        limit: Int = 1000,
        maxCount: Int = 6000,
        accumulator: MutableList<MenuItemsDatabaseData> = mutableListOf(),
        onSuccess: (List<MenuItemsDatabaseData>) -> Unit,
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
        onSuccess: (List<MenuItemsDatabaseData>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {

            val json = context.openFileInput("purchase.json")
                .bufferedReader()
                .use { it.readText() }

            // DBのカラム名とデータクラスのプロパティ名の形式をスネークケースに合わせる
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

            val type = object : TypeToken<List<MenuItemsDatabaseData>>() {}.type
            cachedItems = gson.fromJson(json, type)

            val allItems = cachedItems ?: emptyList()

            val sliced = allItems
                .drop(offset)
                .take(limit)

            cachedItems = null
            onSuccess(sliced)

        } catch (e: Exception) {
            Log.e("fetch data", "error")
            onError(e)
        }
    }

}




