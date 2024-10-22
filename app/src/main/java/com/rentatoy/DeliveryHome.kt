package com.rentatoy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DeliveryHome : AppCompatActivity() {
    private lateinit var ordersBtn: Button

    private lateinit var signOutBtn:Button

    private lateinit var settingsBtn:Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_home)

        signOutBtn=findViewById(R.id.btnSignOut)

        ordersBtn=findViewById(R.id.btnOrders)
        settingsBtn=findViewById(R.id.btnSettings)

        auth = Firebase.auth



        settingsBtn.setOnClickListener {
            startActivity(Intent(this,CustomerProfile::class.java))
        }

        ordersBtn.setOnClickListener {
            startActivity(Intent(this,DeliveryManageOrders::class.java))
        }


        signOutBtn.setOnClickListener {
            Firebase.auth.signOut()

            Toast.makeText(baseContext, "Signed out", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
                .apply {
                    flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            startActivity(intent)

        }
    }
}