package com.example.przewodnik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        backButton = findViewById(R.id.backButton_Help)
        backButton.setOnClickListener(buttonClick)
    }
    lateinit var backButton: Button

    private val buttonClick = View.OnClickListener {
        val intent = Intent(this, OpeningActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}