package com.example.checkexclusivecontrol.purchase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.checkexclusivecontrol.R
import com.example.checkexclusivecontrol.MenuItemsDatabaseData

class ContentAdapter(
    private val items: List<MenuItemsDatabaseData>,
    private val onClick: (MenuItemsDatabaseData) -> Unit
) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentText: TextView = itemView.findViewById(R.id.contentText)

        fun bind(item: MenuItemsDatabaseData) {
            // ★ 表示
            contentText.text = item.storeId

            itemView.setOnClickListener {
                onClick(item) // ← Activity に通知
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
