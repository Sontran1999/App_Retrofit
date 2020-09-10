package com.example.app_demo.Adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_retrofit.data.model.Contact
import com.example.app_retrofit.activity.DetailActivity
import com.example.app_retrofit.activity.NewEmployeeActivity
import com.example.app_retrofit.R


class EmployeeListAdapter(private val onDelete: (Contact) -> Unit) :
    RecyclerView.Adapter<EmployeeListAdapter.ViewHoder>(){
    var mContact: MutableList<Contact> = arrayListOf()

    class ViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.tv_name)
        var image: ImageView = itemView.findViewById(R.id.img_avatar)
        var edit: ImageButton = itemView.findViewById(R.id.btn_edit)
        var delete: ImageButton = itemView.findViewById(R.id.btn_delete)
    }

    override fun getItemCount(): Int {
        return mContact.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val studentView: View = layoutInflater.inflate(R.layout.employee_item, parent, false)
        var viewHoder: ViewHoder = ViewHoder(studentView)
        return viewHoder
    }

    override fun onBindViewHolder(holder: ViewHoder, position: Int) {
        holder.name.text = mContact.get(position).lastName
        holder.image.setImageBitmap(stringToBitMap(mContact.get(position).customFields?.get(0)?.value))
//        Log.d("MainActivity", byteArray.toString());
        holder.image.setOnClickListener {
            var bundle: Bundle = Bundle()
            bundle.putSerializable("object", mContact[position])
            val intent: Intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("data", bundle)
            it.context.startActivity(intent)
        }
        holder.edit.setOnClickListener{
            var bundle: Bundle = Bundle()
            bundle.putSerializable("object", mContact[position])
            bundle.putInt("key2", 2)
            val intent: Intent = Intent(it.context, NewEmployeeActivity::class.java)
            intent.putExtra("data", bundle)
            it.context.startActivity(intent)
        }
        holder.delete.setOnClickListener{
            onDelete(mContact[position])
        }
    }
    fun setList(list: MutableList<Contact>) {
        this.mContact = list
        notifyDataSetChanged()
    }
    fun reset(){
        mContact.clear()
        notifyDataSetChanged()
    }

    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }
}