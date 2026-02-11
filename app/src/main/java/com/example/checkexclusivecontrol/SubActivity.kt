package com.example.checkexclusivecontrol

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.checkexclusivecontrol.purchase.ContentAdapter
import com.example.checkexclusivecontrol.purchase.PurchaseConfirmDialog

class SubActivity : ComponentActivity() {

    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // XML レイアウトを使う場合も setContentView は利用可能
        setContentView(R.layout.activity_list)

        val recyclerView: RecyclerView = findViewById(R.id.userListView)

        appDatabase = AppDatabase(this)

        // データ取得
        val users = appDatabase.getAllUsers()

        val adapter = ContentAdapter(users) { item ->
            PurchaseConfirmDialog.show(this,
                onConfirm = {
                    Log.d("Purchase", "ok")
                },
                onCancel = {
                    Log.d("Purchase", "cancelled")
                }
            )
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}