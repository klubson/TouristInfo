package com.example.przewodnik

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.przewodnikpotoruniu.Object
import java.util.ArrayList

class ObjectListAdapter(
    private val arrayList: ArrayList<Object>,
    private val listener: OnItemClickListener
) :RecyclerView.Adapter<ObjectListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.object_list_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val obj = arrayList[position]
        holder.name.text = obj.name
    }

    override fun getItemCount() = arrayList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val name : TextView = itemView.findViewById(R.id.object_item_name)


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val item = itemView
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int){
        }
    }

}