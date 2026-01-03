package com.example.checkexclusivecontrol

import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // XML レイアウトを使う場合も setContentView は利用可能
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        // データ取得
        val users = databaseHelper.getAllUsers()

        // ListView に表示
        userAdapter = UserAdapter(this, users)
        val userListView: ListView = findViewById(R.id.userListView)
        userListView.adapter = userAdapter
    }
}