package com.example.checkexclusivecontrol

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.sqlite.transaction

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"   // データベース名
        private const val DATABASE_VERSION = 1              // DBバージョン
        private const val TABLE_NAME_USERS = "users"        // テーブル名
    }

    /**
     * データベース初回作成時
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UsersTable.CREATE)
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
                db.execSQL(UsersTable.ADD_AGE) // sample
            }
            2 -> {
                db.execSQL(UsersTable.INDEX_EMAIL) //sample
            }
        }
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
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(UsersTable.USERS_COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(UsersTable.USERS_COLUMN_NAME))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(UsersTable.USERS_COLUMN_EMAIL))

            users.add(User(id, name, email))
        }

        cursor.close()
        db.close()
        return users
    }

    fun insertUsers(users: List<User>) {
        val db = writableDatabase
        db.transaction {
            for (user in users) {
                val values = ContentValues().apply {
                    put(UsersTable.USERS_COLUMN_NAME, user.name)
                    put(UsersTable.USERS_COLUMN_EMAIL, user.email)
                }
                insert(TABLE_NAME_USERS, null, values)
            }
        }
    }

    /**
     * users テーブルを全件クリア
     */
    fun clearUsers() {
        val db = writableDatabase
        db.transaction {
            delete(TABLE_NAME_USERS, null, null)
        }
    }
}


object UsersTable {

    const val TABLE = "users"

    const val USERS_COLUMN_ID = "id"            // カラム1
    const val USERS_COLUMN_NAME = "name"        // カラム2
    const val USERS_COLUMN_EMAIL = "email"      // カラム3
    const val COL_AGE = "age"

    val CREATE = """
        CREATE TABLE $TABLE (
            $USERS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $USERS_COLUMN_NAME TEXT,
            $USERS_COLUMN_EMAIL TEXT
        )
    """.trimIndent()

    const val ADD_AGE =
        "ALTER TABLE $TABLE ADD COLUMN $COL_AGE INTEGER DEFAULT 0"

    const val INDEX_EMAIL =
        "CREATE INDEX idx_users_email ON $TABLE($USERS_COLUMN_EMAIL)"
}

