package com.example.app_demo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_retrofit.Data.Model.Data
import com.example.app_retrofit.R


class EmployeeListAdapter( var mContext: Context, var mData: List<Data>) :
    RecyclerView.Adapter<EmployeeListAdapter.ViewHoder>(){

    class ViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: TextView = itemView.findViewById(R.id.tv_id)
        var name: TextView = itemView.findViewById(R.id.tv_name)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val studentView: View = layoutInflater.inflate(R.layout.employee_item, parent, false)
        var viewHoder: ViewHoder = ViewHoder(studentView)
        return viewHoder
    }

    override fun onBindViewHolder(holder: ViewHoder, position: Int) {
        holder.id.text = mData[position].id.toString()
        holder.name.text = mData[position].name
    }
    fun setList(list: List<Data>?) {
        if (list != null) {
            this.mData = list
        }
        notifyDataSetChanged()
    }
}