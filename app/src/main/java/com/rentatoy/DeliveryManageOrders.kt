package com.rentatoy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class DeliveryManageOrders : AppCompatActivity() {
    private lateinit var deliveryPendingBtn: Button
    private lateinit var deliveredBtn: Button
    private lateinit var returnsBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_manage_orders)

        deliveryPendingBtn=findViewById(R.id.btnDeliveryPending)
        deliveredBtn=findViewById(R.id.btnDelivered)
        returnsBtn=findViewById(R.id.btnReturns)

        deliveryPendingBtn.setOnClickListener {
            startActivity(Intent(this,DeliveryPendning::class.java))
        }
        deliveredBtn.setOnClickListener {
            startActivity(Intent(this,DeliveryHistory::class.java))
        }

        returnsBtn.setOnClickListener {
            startActivity(Intent(this,Returns::class.java))
        }
    }
}