package com.example.roomdb

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Dao
import com.example.roomdb.DAO.UserDao
import com.example.roomdb.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyAdapter(private val dataList: List<User>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    lateinit var btnDlete: Button
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentItem = dataList[position]
        holder.nametv.text =currentItem.firstName + " " +currentItem.firstName
//        notifyItemInserted(position)

        holder.deletebtn.setOnClickListener {

            if(onClickListener != null){
                onClickListener!!.onClick(position, currentItem)

                Log.e("===>", "Deleted by id: ${currentItem.id}")
            }else{
                Log.e("===>", "Not Deleted : ${currentItem.id}")
            }
        }
    }
//            (holder.deletebtn.context as AppCompatActivity).lifecycleScope.launch(Dispatchers.IO){
//                userDao.deleteById(currentItem.id)
//            }

    override fun getItemCount(): Int {
         return dataList.size
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener{
        fun onClick(position: Int, model: User)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nametv: TextView = itemView.findViewById(R.id.tvName)
        val deletebtn: Button = itemView.findViewById(R.id.btnDelete)
    }
}