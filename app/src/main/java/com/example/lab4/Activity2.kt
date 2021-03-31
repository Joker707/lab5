package com.example.lab4

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity2.nav_view

class Activity2: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2)
        nav_view.setNavigationItemSelectedListener {
            startActivity(Intent(this, ActivityAbout::class.java))
            return@setNavigationItemSelectedListener true
        }
    }

    fun toFirst(view: View) {
        finish()
    }

    fun toThird(view: View) {
        startActivity(Intent(this, Activity3::class.java))
    }

}