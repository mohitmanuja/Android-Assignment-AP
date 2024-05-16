package com.example.baseandroid.base.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.baseandroid.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}