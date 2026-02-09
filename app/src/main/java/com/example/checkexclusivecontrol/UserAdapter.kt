package com.example.checkexclusivecontrol

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class UserAdapter(
    private val context: Context,
    private val users: List<User>
) : BaseAdapter() {

    // アダプターが管理するアイテム数を返す
    override fun getCount(): Int {
        return users.size
    }

    override fun getItem(position: Int): User {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // 指定された位置のアイテムのビューを返す
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
        }

        val user = getItem(position)
        val text1 = view!!.findViewById<TextView>(android.R.id.text1)
        val text2 = view.findViewById<TextView>(android.R.id.text2)

        text1.text = user.name
        text2.text = user.email

        return view
    }
}