package com.example.apis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class MyAdapter(val context: Context, private var originalList: List<mydataItem>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var filteredList: MutableList<mydataItem> = originalList.toMutableList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userId: TextView = itemView.findViewById(R.id.userid)
        val title: TextView = itemView.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.userId.text = currentItem.userId.toString()
        holder.title.text = currentItem.title
    }

    override fun getItemCount(): Int = filteredList.size

    fun filter(query: String?) {
        filteredList.clear()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(originalList)
        } else {
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            filteredList.addAll(originalList.filter {
                it.title.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
            })
        }
        notifyDataSetChanged()
    }

    fun updateData(newData: List<mydataItem>) {
        originalList = newData
        filteredList = newData.toMutableList()
        notifyDataSetChanged()
    }
}
