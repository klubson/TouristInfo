package com.example.przewodnik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.przewodnikpotoruniu.DBHelper

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        thread.start()
    }
    lateinit var db: DBHelper

    val thread = Thread(){
        run {
            db = DBHelper(this)
            db.getJSONFile(this)
        }
        runOnUiThread(){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}