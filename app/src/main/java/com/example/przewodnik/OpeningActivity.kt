package com.example.przewodnik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class OpeningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)
        startButton = findViewById(R.id.startButton_Main)
        startButton.setOnClickListener(startClick)
        helpButton = findViewById(R.id.helpButton_Main)
        helpButton.setOnClickListener(helpClick)
        exitButton = findViewById(R.id.exitButton_Main)
        exitButton.setOnClickListener(exitClick)
    }
    lateinit var startButton: Button
    lateinit var helpButton: Button
    lateinit var exitButton: Button

    private val startClick = View.OnClickListener {
        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    private val helpClick = View.OnClickListener {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    private val exitClick = View.OnClickListener {
        finishAffinity()
    }
}