package com.example.devappforerrors.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devappforerrors.R

class ErrorAdapter() : RecyclerView.Adapter<ErrorAdapter.ViewHolder>() {

    lateinit var context: Context
    lateinit var listItems: MutableList<ErrorListModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.error_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.error.text = listItems[position].error
        holder.from.text = listItems[position].timeStamp
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val from = itemView.findViewById(R.id.time_tv) as TextView
        val error = itemView.findViewById(R.id.error_tv) as TextView
    }

    constructor(context: Context, listItems: MutableList<ErrorListModel>) : this() {
        this.context = context
        this.listItems = listItems
    }
}