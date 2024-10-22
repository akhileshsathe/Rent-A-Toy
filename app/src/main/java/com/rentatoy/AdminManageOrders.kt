package com.rentatoy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminManageOrders : AppCompatActivity() {
    private lateinit var approvedOrdersBtn: Button
    private lateinit var pendingOrdersBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_orders)

        approvedOrdersBtn=findViewById(R.id.btnApprovedOrders)
        pendingOrdersBtn=findViewById(R.id.btPendingOrders)
        approvedOrdersBtn.setOnClickListener {
            startActivity(Intent(this,AdminManageApprovedOrders::class.java))
        }
        pendingOrdersBtn.setOnClickListener {
            startActivity(Intent(this,AdminViewPending::class.java))
        }
    }
}