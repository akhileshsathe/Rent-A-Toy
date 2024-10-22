package com.rentatoy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Login : AppCompatActivity() {

    private lateinit var signUpTxt: TextView
    private lateinit var resetPasswordBtn: TextView
    private lateinit var loginBtn:Button
    private lateinit var emailEdt: EditText
    private lateinit var passwordEdt: EditText

    private lateinit var emailLayoutEdt: TextInputLayout
    private lateinit var passLayoutEdt: TextInputLayout
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val sharedPrefs=getSharedPreferences("RentAToy",MODE_PRIVATE)
        val mEditor = sharedPrefs.edit()

        loginBtn=findViewById(R.id.btnLogin)
        signUpTxt=findViewById(R.id.txtSignup)
        resetPasswordBtn=findViewById(R.id.txtResetPassword)

        emailLayoutEdt = findViewById(R.id.edtEmailLayout)
        passLayoutEdt = findViewById(R.id.edtPassTxtInpLayout)

        emailEdt=findViewById(R.id.edtEmail)
        passwordEdt=findViewById(R.id.edtPass)


        resetPasswordBtn.setOnClickListener {
            val intent = Intent(this,ResetPassword::class.java)
            startActivity(intent)
        }
        loginBtn.setOnClickListener {

            val email=emailEdt.text.toString()
            val password=passwordEdt.text.toString()


            if(email.isEmpty()){
                emailLayoutEdt.error = "Required *"


            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailLayoutEdt.error = "Invalid Email Address"


            }
            else if(password.isEmpty()){
                emailLayoutEdt.error=null
                passLayoutEdt.error = "Required *"



            }
            else if(password.length<6){
                emailLayoutEdt.error=null
                passLayoutEdt.error = "Password is too short"

            }
            else{
                passLayoutEdt.error=null
                val tag= this.localClassName
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

//                            val currentUser=
                            FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child(auth.currentUser?.uid.toString())
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot)
                                    {

                                        val user=snapshot.getValue(UserHelper::class.java)
                                        when (val userType=user?.userType) {
                                            "A" -> {
                                                mEditor.putString("userType",userType)
                                                mEditor.apply()
                                                val intent = Intent(baseContext, AdminHome::class.java).apply {
                                                        flags =
                                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                startActivity(intent)

                                            }

                                            "C" -> {
                                                mEditor.putString("userType",userType)
                                                mEditor.apply()
                                                val intent = Intent(baseContext, UserHome::class.java).apply {
                                                        flags =
                                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }
                                                startActivity(intent)


                                            }

                                            "D" -> {
                                                mEditor.putString("userType",userType)
                                                mEditor.apply()
                                                val intent = Intent(baseContext, DeliveryHome::class.java).apply {
                                                        flags =
                                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    }

                                                startActivity(intent)

                                            }
                                        }

                                        }

                                    override fun onCancelled(error: DatabaseError) {

                                    }
                                })


                            Log.d(tag, "signInWithEmail:success")
                            mEditor.apply()






//                        updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(tag, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
//                        updateUI(null)
                        }
                    }
            }
            }




        signUpTxt.setOnClickListener {
            startActivity(Intent(this,Signup::class.java))
        }
    }
}