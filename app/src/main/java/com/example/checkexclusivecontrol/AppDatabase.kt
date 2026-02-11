package com.example.checkexclusivecontrol

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.sqlite.transaction
import com.example.checkexclusivecontrol.purchase.MenuItemsTable

class AppDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"   // データベース名
        private const val DATABASE_VERSION = 1              // DBバージョン
        private const val TABLE_NAME_MENU_ITEMS = "menu_items"        // テーブル名
    }

    /**
     * データベース初回作成時
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MenuItemsTable.CREATE)
    }

    /**
     * データベースアップグレード
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        var version = oldVersion

        db.transaction {
            while (version < newVersion) {
                upgrade(version,this)
                version++
            }
        }
    }

    /**
     * マイグレーション処理
     */
    private fun upgrade(version: Int,db: SQLiteDatabase) {
        when (version) {
            1 -> {
                db.execSQL(MenuItemsTable.ADD_AGE) // sample
            }
            2 -> {
                db.execSQL(MenuItemsTable.INDEX_EMAIL) //sample
            }
        }
    }

    /**
     * 全ユーザー取得
     */
    fun getAllUsers(): List<MenuItemsDatabaseData> {
        val menuItemsDatabaseData = mutableListOf<MenuItemsDatabaseData>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME_MENU_ITEMS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(MenuItemsTable.MENU_ITEMS_COLUMN_ID))
            val storeId = cursor.getString(cursor.getColumnIndexOrThrow(MenuItemsTable.MENU_ITEMS_COLUMN_STORE_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(MenuItemsTable.MENU_ITEMS_COLUMN_NAME))

            menuItemsDatabaseData.add(MenuItemsDatabaseData(id, storeId, name))
        }

        cursor.close()
        db.close()
        return menuItemsDatabaseData
    }

    fun insertUsers(menuItemsDatabaseData: List<MenuItemsDatabaseData>) {
        val db = writableDatabase
        db.transaction {
            for (user in menuItemsDatabaseData) {
                val values = ContentValues().apply {
                    put(MenuItemsTable.MENU_ITEMS_COLUMN_STORE_ID, user.storeId)
                    put(MenuItemsTable.MENU_ITEMS_COLUMN_NAME, user.name)
                }
                insert(TABLE_NAME_MENU_ITEMS, null, values)
            }
        }
    }

    /**
     * users テーブルを全件クリア
     */
    fun clearUsers() {
        val db = writableDatabase
        db.transaction {
            delete(TABLE_NAME_MENU_ITEMS, null, null)
        }
    }
}