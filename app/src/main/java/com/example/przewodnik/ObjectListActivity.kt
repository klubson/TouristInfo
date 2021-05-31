package com.example.przewodnik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.przewodnikpotoruniu.DBHelper
import com.example.przewodnikpotoruniu.Object

class ObjectListActivity : AppCompatActivity(), ObjectListAdapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_list)
        val list: RecyclerView = findViewById(R.id.objectList_List)
        val databaseHandler = DBHelper(this)
        objectList = databaseHandler.objects
        val objectListAdapter : ObjectListAdapter = ObjectListAdapter(objectList, this)
        list.adapter = objectListAdapter
        list.layoutManager = LinearLayoutManager(this)
        list.setHasFixedSize(true)
    }
    lateinit var objectList: ArrayList<Object>
    override fun onItemClick(position: Int) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("name", objectList[position].name)
        startActivity(intent)
        this.finish()
    }
}