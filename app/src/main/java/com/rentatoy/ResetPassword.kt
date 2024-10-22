package com.rentatoy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ResetPassword : AppCompatActivity() {
    private lateinit var emailEdt: EditText
    private lateinit var emailLayoutEdt: TextInputLayout

    private lateinit var loginBtnTxt : TextView

    private lateinit var resetPasswordBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        emailEdt=findViewById(R.id.edtEmail)
        emailLayoutEdt = findViewById(R.id.edtEmailLayout)
        resetPasswordBtn = findViewById(R.id.btnResetPassword)


        loginBtnTxt=findViewById(R.id.txtLoginBtn)

        loginBtnTxt.setOnClickListener {
            val intent = Intent(this, Login::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            startActivity(intent)
        }

        resetPasswordBtn.setOnClickListener {

            val email=emailEdt.text.toString()
            if(email.isEmpty()){
                emailLayoutEdt.error = "Required *"


            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailLayoutEdt.error = "Invalid Email Address"


            }
            else{

                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Reset password link has been sent.", Toast.LENGTH_SHORT).show()
                            Log.d("Reset password", "Email sent.")
                        }
                        else{
                            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()

                        }

                    }
            }
        }

    }
}