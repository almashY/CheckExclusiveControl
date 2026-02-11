package com.example.checkexclusivecontrol.purchase

object MenuItemsTable {

    const val TABLE = "menu_items"

    const val MENU_ITEMS_COLUMN_ID = "id"            // カラム1
    const val MENU_ITEMS_COLUMN_STORE_ID = "store_id"        // カラム2
    const val MENU_ITEMS_COLUMN_NAME = "name"      // カラム3
    const val COL_AGE = "age"

    val CREATE = """
        CREATE TABLE $TABLE (
            $MENU_ITEMS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $MENU_ITEMS_COLUMN_STORE_ID TEXT,
            $MENU_ITEMS_COLUMN_NAME TEXT
        )
    """.trimIndent()

    const val ADD_AGE =
        "ALTER TABLE $TABLE ADD COLUMN $COL_AGE INTEGER DEFAULT 0"

    const val INDEX_EMAIL =
        "CREATE INDEX idx_users_email ON $TABLE($MENU_ITEMS_COLUMN_NAME)"
}