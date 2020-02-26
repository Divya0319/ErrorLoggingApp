package com.example.devappforerrors.model

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devappforerrors.ErrorActivity
import com.example.devappforerrors.R

class UserEmailAdapter() : RecyclerView.Adapter<UserEmailAdapter.ViewHolder>() {

    lateinit var context: Context
    lateinit var listItems: MutableList<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.email_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.from.text = listItems[position]

        holder.linearLayout.setOnClickListener{
            val intent = Intent(context, ErrorActivity::class.java)
            intent.putExtra("email",listItems[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val from = itemView.findViewById(R.id.email_tv) as TextView
        val linearLayout = itemView.findViewById(R.id.emailHolderLL) as LinearLayout
    }

    constructor(context: Context, listItems: MutableList<String>) : this() {
        this.context = context
        this.listItems = listItems
    }
}