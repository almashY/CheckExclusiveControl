package com.example.checkexclusivecontrol.purchase


import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkexclusivecontrol.R
import org.json.JSONObject


class PurchaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_list)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // ① 表示するデータ
        val items = listOf(
            ContentItem(1, "コンテンツA"),
            ContentItem(2, "コンテンツB"),
            ContentItem(3, "コンテンツC")
        )

        // ② Adapter 作成（クリック時の処理をここで定義）
        val adapter = ContentAdapter(items) { item ->
            writeJson(item)
        }

        // ③ RecyclerView にセット
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun writeJson(item: ContentItem) {
        val json = JSONObject().apply {
            put("id", item.id)
            put("contentName", item.contentName)
        }

        openFileOutput("purchase.json", MODE_PRIVATE).use {
            it.write(json.toString().toByteArray())
        }
    }
}
