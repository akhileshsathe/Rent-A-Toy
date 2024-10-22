package com.rentatoy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminManageUsers : AppCompatActivity() {
    private lateinit var customerBtn: Button
    private lateinit var deliveryPartnerBtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_users)

        customerBtn=findViewById(R.id.btnCustomers)
        deliveryPartnerBtn=findViewById(R.id.btnDelivery)
        customerBtn.setOnClickListener {
            startActivity(Intent(this,AdminViewCustomer::class.java))
        }
        deliveryPartnerBtn.setOnClickListener {
            startActivity(Intent(this,AdminViewDeliveryPartner::class.java))
        }
    }
}