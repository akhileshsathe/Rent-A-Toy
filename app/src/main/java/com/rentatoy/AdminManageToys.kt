package com.rentatoy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminManageToys : AppCompatActivity() {
    private lateinit var addToyBtn:Button
    private lateinit var viewToysBtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_toys)
        addToyBtn=findViewById(R.id.btnAddToy)
        viewToysBtn=findViewById(R.id.btnViewToy)

        addToyBtn.setOnClickListener {
            startActivity(Intent(this,AdminAddToy::class.java))
        }
        viewToysBtn.setOnClickListener {
            startActivity(Intent(this,AdminViewToys::class.java))
        }
    }
}