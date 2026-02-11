package com.example.checkexclusivecontrol.purchase


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkexclusivecontrol.R
import com.example.checkexclusivecontrol.MenuItemsDatabaseData
import org.json.JSONArray
import org.json.JSONObject


class PurchaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_list)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // ① 表示するデータ
        val items = listOf(
            MenuItemsDatabaseData(1, "店名1", "味噌ラーメン")
        )

        // ② Adapter 作成（クリック時の処理をここで定義）
        val adapter = ContentAdapter(items) { item ->
            PurchaseConfirmDialog.show(this,
                onConfirm = {
                    writeJson(item)
                    Log.d("Purchase", "ok")
                },
                onCancel = {
                    Log.d("Purchase", "cancelled")
                }
            )
        }

        // ③ RecyclerView にセット
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun writeJson(item: MenuItemsDatabaseData) {
        val jsonArray = readJsonArray()

        val newItem = JSONObject().apply {
            put("id", item.id)
            put("store_id", item.storeId)
            put("name", item.name)
        }

        jsonArray.put(newItem)

        openFileOutput("purchase.json", MODE_PRIVATE).use {
            it.write(jsonArray.toString().toByteArray())
        }
    }

    private fun readJsonArray(): JSONArray {
        return try {
            val text = openFileInput("purchase.json")
                .bufferedReader()
                .use { it.readText() }

            JSONArray(text)
        } catch (e: Exception) {
            // 初回（ファイルがない・空）
            JSONArray()
        }
    }

}
