package com.rentatoy

import android.content.Intent
//import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Set status bar colour
//        window.statusBarColor = Color.parseColor("#36E1FF");

        auth = Firebase.auth
        val currentUser=auth.currentUser
        val sharedPrefs=getSharedPreferences("RentAToy",MODE_PRIVATE)
        val userType=sharedPrefs.getString("userType","")

        Handler(Looper.getMainLooper()).postDelayed({
            if(currentUser!=null){
                when(userType){
                    "A"->{
                        val intent = Intent(this, AdminHome::class.java)
                            .apply {
                                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        startActivity(intent)

                    }
                    "C"->{
                        val intent = Intent(this, UserHome::class.java)
                            .apply {
                                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        startActivity(intent)


                    }
                    "D"->{
                        val intent = Intent(this, DeliveryHome::class.java)
                            .apply {
                                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        startActivity(intent)

                    }
                    ""->{
                        Toast.makeText(baseContext, "Unknown user", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Login::class.java)
                            .apply {
                                flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                        startActivity(intent)
                    }

                }

//            finish()
            }
            else{
                val intent = Intent(this, Login::class.java)
                    .apply {
                        flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                startActivity(intent)
            }



        },  1000)

    }

}





