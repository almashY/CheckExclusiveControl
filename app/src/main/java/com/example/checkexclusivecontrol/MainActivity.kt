package com.example.checkexclusivecontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val apiWrapper = APIWrapper(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            //ここから遷移用のコード
            apiWrapper.fetchAllItemsRecursively(
                onSuccess = { items ->
                    // 6000件すべて揃った状態
                    println("取得完了：${items.size}件")
                    // DB保存・Adapter更新など
                },
                onError = { error ->
                    error.printStackTrace()
                    // エラー表示
                }
            )
            val intent = Intent(this, SubActivity::class.java)    //intentインスタンスの生成(第二引数は遷移先のktファイル名)
            startActivity(intent)
            //ここまで
        }
    }
}