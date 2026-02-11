package com.example.checkexclusivecontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.example.checkexclusivecontrol.purchase.PurchaseActivity

class MainActivity : ComponentActivity() {

    private val apiWrapper = APIWrapper(this)
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appDatabase = AppDatabase(this)

        val button: Button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            //ここから遷移用のコード
            apiWrapper.fetchAllItemsRecursively(
                onSuccess = { items ->
                    // 6000件すべて揃った状態
                    println("取得完了：${items.size}件")
                    // DB保存・Adapter更新など
                    val menuItems = items.map {
                        MenuItemsDatabaseData(it.id, it.storeId, it.name)
                    }

                    // ① 既存データをクリア
                    appDatabase.clearUsers()
                    // ② 新しいデータを保存
                    appDatabase.insertUsers(menuItems)
                },
                onError = { error ->
                    error.printStackTrace()
                    // エラー表示
                }
            )

            // ③ 画面遷移
            val intent = Intent(this, SubActivity::class.java)    //intentインスタンスの生成(第二引数は遷移先のktファイル名)
            startActivity(intent)
            //ここまで
        }

        val purchaseButton: Button = findViewById<Button>(R.id.purchase_button)
        purchaseButton.setOnClickListener{
            // 画面遷移
            val intent = Intent(this, PurchaseActivity::class.java)    //intentインスタンスの生成(第二引数は遷移先のktファイル名)
            startActivity(intent)
            //ここまで
        }


    }
}