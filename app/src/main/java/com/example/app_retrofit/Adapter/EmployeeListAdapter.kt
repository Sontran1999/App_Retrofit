package com.example.app_demo.Adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.app_retrofit.Data.Model.Contact
import com.example.app_retrofit.Data.Model.Employee
import com.example.app_retrofit.Data.Remote.APIService
import com.example.app_retrofit.Data.Remote.ApiUtils
import com.example.app_retrofit.DetailActivity
import com.example.app_retrofit.NewEmployeeActivity
import com.example.app_retrofit.R
import retrofit2.Response


class EmployeeListAdapter(var mContext: Context) :
    RecyclerView.Adapter<EmployeeListAdapter.ViewHoder>(){
    var mService: APIService? = ApiUtils().getAPIService()
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
            delete(position)
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
    fun delete(position: Int){
        var alertDialog = AlertDialog.Builder(mContext)
        alertDialog.setMessage("Are you sure you want to delete")
        alertDialog.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialogInterface, i ->
                mService?.delete(mContact[position].contactId)
                    ?.enqueue(object : retrofit2.Callback<Unit> {
                        override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                            Log.d("MainActivity", "error");
                            Toast.makeText(mContext, "delete error", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: retrofit2.Call<Unit>,
                            response: Response<Unit>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("MainActivity", "delete successfully")
                                load()
                                Toast.makeText(mContext, "delete successfully", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })

            }
        )
        alertDialog.setNegativeButton(
            "Cancel ",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })

        alertDialog.show()
    }
     fun load(){
         mService?.getAll()?.enqueue(object : retrofit2.Callback<Employee> {
             override fun onFailure(call: retrofit2.Call<Employee>, t: Throwable) {
                 Log.d("MainActivity", "error loading from API");
                 Toast.makeText(mContext, "error loading from API", Toast.LENGTH_SHORT).show()
             }

             override fun onResponse(call: retrofit2.Call<Employee>, response: Response<Employee>) {
                 if (response.isSuccessful) {
                     reset()
                     setList(response.body()?.contacts as MutableList<Contact>)
                     Log.d("MainActivity", "posts loaded from API")
                 }
             }
         })
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