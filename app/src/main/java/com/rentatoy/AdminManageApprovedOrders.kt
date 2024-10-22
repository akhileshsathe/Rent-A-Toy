package com.rentatoy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminManageApprovedOrders : AppCompatActivity() {
    private lateinit var deliveryPendingBtn: Button
    private lateinit var deliveredBtn: Button
    private lateinit var returnedBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_approved_orders)

        deliveryPendingBtn=findViewById(R.id.btnDeliveryPending)
        deliveredBtn=findViewById(R.id.btnDelivered)
        returnedBtn=findViewById(R.id.btnReturned)

        deliveryPendingBtn.setOnClickListener {
            startActivity(Intent(this,AdminViewDelieryPending::class.java))
        }
        deliveredBtn.setOnClickListener {
            startActivity(Intent(this,AdminViewDelivered::class.java))
        }

        returnedBtn.setOnClickListener {
            startActivity(Intent(this,AdminViewReturned::class.java))
        }
    }
}