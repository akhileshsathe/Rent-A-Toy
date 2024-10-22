package com.rentatoy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminHome : AppCompatActivity() {
    private lateinit var ordersBtn: Button
    private lateinit var manageToysBtn:Button
    private lateinit var manageUsersBtn:Button
    private lateinit var signOutBtn:Button
    private lateinit var dashBtn:Button
    private lateinit var btnAdminSettings:Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        manageUsersBtn=findViewById(R.id.btnManageUsers)
        manageToysBtn=findViewById(R.id.btnManageToys)
        signOutBtn=findViewById(R.id.btnSignOut)
        dashBtn=findViewById(R.id.btnDash)
        ordersBtn=findViewById(R.id.btnOrders)
        btnAdminSettings=findViewById(R.id.btnAdminSettings)


        auth = Firebase.auth

        btnAdminSettings.setOnClickListener {
            startActivity(Intent(this,CustomerProfile::class.java))
        }

        manageToysBtn.setOnClickListener {
        startActivity(Intent(this,AdminManageToys::class.java))
        }
        manageUsersBtn.setOnClickListener {
            startActivity(Intent(this,AdminManageUsers::class.java))
        }
        dashBtn.setOnClickListener {
            startActivity(Intent(this,AdminDashBoard::class.java))
        }
        ordersBtn.setOnClickListener {
            startActivity(Intent(this,AdminManageOrders::class.java))
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