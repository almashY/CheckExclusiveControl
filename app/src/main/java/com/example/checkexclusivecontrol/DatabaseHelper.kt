package com.example.checkexclusivecontrol

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"   // データベース名
        private const val DATABASE_VERSION = 1              // DBバージョン
        private const val TABLE_NAME_USERS = "users"        // テーブル名
        private const val USERS_COLUMN_ID = "id"            // カラム1
        private const val USERS_COLUMN_NAME = "name"        // カラム2
        private const val USERS_COLUMN_EMAIL = "email"      // カラム3
    }

    /**
     * データベース初回作成時
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME_USERS (
                $USERS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $USERS_COLUMN_NAME TEXT,
                $USERS_COLUMN_EMAIL TEXT
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    /**
     * データベースアップグレード
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_USERS")
        onCreate(db)
    }

    /**
     * 全ユーザー取得
     */
    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME_USERS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_NAME))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_EMAIL))

            users.add(User(id, name, email))
        }

        cursor.close()
        db.close()
        return users
    }

    fun insertUsers(users: List<User>) {
        val db = writableDatabase
        db.beginTransaction()

        try {
            for (user in users) {
                val values = ContentValues().apply {
                    put(USERS_COLUMN_NAME, user.name)
                    put(USERS_COLUMN_EMAIL, user.email)
                }
                db.insert(TABLE_NAME_USERS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    /**
     * users テーブルを全件クリア
     */
    fun clearUsers() {
        val db = writableDatabase
        db.beginTransaction()

        try {
            db.delete(TABLE_NAME_USERS, null, null)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

}
