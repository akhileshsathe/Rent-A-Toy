package com.rentatoy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.util.Calendar


class Signup : AppCompatActivity() {
    private lateinit var loginButton: TextView
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confPassword: EditText
    private lateinit var fullNameEdt: EditText
    private lateinit var phoneEdt: EditText
    private lateinit var addressEdt: EditText
    private lateinit var dobEdt: EditText
    private lateinit var cityEdt:EditText
    private lateinit var signupButton: Button



    private lateinit var auth: FirebaseAuth


    private lateinit var userTypeSpinner: AutoCompleteTextView
    private lateinit var userTypeInputLayout: TextInputLayout
    private lateinit var fullNameLayoutEdt: TextInputLayout
    private lateinit var emailLayoutEdt: TextInputLayout
    private lateinit var phoneLayoutEdt: TextInputLayout
    private lateinit var passLayoutEdt: TextInputLayout
    private lateinit var addressLayoutEdt: TextInputLayout
    private lateinit var cityLayoutEdt: TextInputLayout


    private lateinit var dobLayoutEdt: TextInputLayout
    private lateinit var confPassLayoutEdt: TextInputLayout





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val sharedPrefs=getSharedPreferences("RentAToy",MODE_PRIVATE)
        val mEditor = sharedPrefs.edit()
        //Firebase
        auth = Firebase.auth

        //EditText
        email = findViewById(R.id.edtEmail)
        password = findViewById(R.id.edtPass)
        confPassword = findViewById(R.id.edtConfPass)
        fullNameEdt = findViewById(R.id.edtFullName)
        phoneEdt = findViewById(R.id.edtPhone)
        addressEdt = findViewById(R.id.edtAddress)
        fullNameEdt = findViewById(R.id.edtFullName)

        //buttons
        loginButton = findViewById(R.id.txtLoginBtn)
        signupButton = findViewById(R.id.btnSignUp)
        dobEdt = findViewById(R.id.edtDOB)
        cityEdt=findViewById(R.id.edtCity)
        var userType = ""
        //InputLayout
        fullNameLayoutEdt = findViewById(R.id.edtFullNameLayout)

        passLayoutEdt = findViewById(R.id.edtPassTxtInpLayout)
        confPassLayoutEdt = findViewById(R.id.edtConfPassLayout)
        userTypeInputLayout = findViewById(R.id.inputLayoutUserType)
        emailLayoutEdt = findViewById(R.id.edtEmailLayout)
        phoneLayoutEdt = findViewById(R.id.edtPhoneLayout)
        addressLayoutEdt = findViewById(R.id.edtAddressLayout)
        cityLayoutEdt=findViewById(R.id.edtCityLayout)
        dobLayoutEdt = findViewById(R.id.edtDOBLayout)


        //Spinner
        userTypeSpinner = findViewById(R.id.spinnerUserType)
        val userTypeList = arrayOf("Admin", "Customer", "Delivery Partner")

        val userTypeArrayAdapter = ArrayAdapter(this, R.layout.layout_spinner_item, userTypeList)
        userTypeSpinner.setAdapter(userTypeArrayAdapter)
        dobLayoutEdt.visibility = View.GONE
        addressLayoutEdt.visibility = View.GONE
        cityLayoutEdt.visibility=View.GONE
        userTypeSpinner.setOnItemClickListener { parent, _, position, _ ->

            //Toast.makeText(baseContext, item, Toast.LENGTH_SHORT).show()
            when (parent.getItemAtPosition(position).toString()) {
                "Admin" -> {
                    dobLayoutEdt.visibility = View.GONE
                    addressLayoutEdt.visibility = View.GONE
                    cityLayoutEdt.visibility=View.GONE
                    userType = "A"
                }

                "Customer" -> {
                    dobLayoutEdt.visibility = View.GONE
                    addressLayoutEdt.visibility = View.VISIBLE
                    cityLayoutEdt.visibility=View.VISIBLE
                    userType = "C"


                }

                "Delivery Partner" -> {
                    userType = "D"
                    addressLayoutEdt.visibility = View.GONE
                    cityLayoutEdt.visibility=View.VISIBLE
                    dobLayoutEdt.visibility = View.VISIBLE

                }
            }


        }

        userTypeInputLayout.setEndIconOnClickListener {
            userTypeSpinner.showDropDown()
        }


        loginButton.setOnClickListener {

            val intent = Intent(this, Login::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            startActivity(intent)
        }
        dobEdt.setOnFocusChangeListener { view, _ ->
            if (view.isFocused) {
                view.performClick()
            }
        }
        dobEdt.setOnClickListener {

            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")

                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            datePicker.show(
                supportFragmentManager,
                ""
            )
            datePicker.addOnPositiveButtonClickListener { selectedDateMillis ->
                // Convert the selected date from milliseconds to a Calendar instance
                val selectedDate = Calendar.getInstance()
                selectedDate.timeInMillis = selectedDateMillis

                // Now you can access various components of the selected date
                val year = selectedDate.get(Calendar.YEAR)
                val month = selectedDate.get(Calendar.MONTH) // Note: Months are zero-based
                val day = selectedDate.get(Calendar.DAY_OF_MONTH)
                val date = "$day/$month/$year"
                // Do something with the selected date, for example, print it

                dobEdt.setText(date)
            }

        }
        signupButton.setOnClickListener {
//            Toast.makeText(baseContext, "Signup button pressed", Toast.LENGTH_SHORT).show()
            val emailTxt = email.text.toString()
            val passTxt = password.text.toString()
            val confPassTxt = confPassword.text.toString()
            val fullNameTxt = fullNameEdt.text.toString()
            val phoneTxt = phoneEdt.text.toString()
            val addressTxt = addressEdt.text.toString()
            val dobTxt = dobEdt.text.toString()
            val cityTxt=cityEdt.text.toString()


            if (userType == "") {
                userTypeInputLayout.error = "User Type is required"
            }
            else if (fullNameTxt.isEmpty()) {
                userTypeInputLayout.error = null
                fullNameLayoutEdt.error = "Required *"
            }
            else if (emailTxt.isEmpty()) {
                fullNameLayoutEdt.error = null
                emailLayoutEdt.error = "Required *"

            }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
                fullNameLayoutEdt.error = null
                emailLayoutEdt.error = "Enter a valid email"
            }
            else if (phoneTxt.isEmpty()) {
                emailLayoutEdt.error = null
                phoneLayoutEdt.error = "Required *"

            }
            else if (phoneTxt.length < 10 || phoneTxt.length > 10) {
                emailLayoutEdt.error = null
                phoneLayoutEdt.error = "Invalid phone"
            }
            else if (userType == "C" && addressTxt.isEmpty()) {
                phoneLayoutEdt.error = null
                addressLayoutEdt.error = "Required *"

                }
            else if (userType == "D" && dobTxt.isEmpty()) {
                phoneLayoutEdt.error = null
                dobLayoutEdt.error = "Required *"

                }
            else if ((userType=="C" || userType=="D") && cityTxt.isEmpty()){
                addressLayoutEdt.error = null
                phoneLayoutEdt.error = null
                dobLayoutEdt.error = null
                cityLayoutEdt.error="Required *"
            }
            else if (passTxt.isEmpty()) {
                cityLayoutEdt.error=null
                    addressLayoutEdt.error = null
                    phoneLayoutEdt.error = null
                    dobLayoutEdt.error = null
                passLayoutEdt.error = "Required *"


                } else if (passTxt.length < 6) {
                    phoneLayoutEdt.error = null
                    passLayoutEdt.error = "Password is too short"
                    Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show()
                } else if (confPassTxt.isEmpty()) {
                    passLayoutEdt.error = null
                    confPassLayoutEdt.error = "Required *"


                } else if (passTxt != confPassTxt) {
                    passLayoutEdt.error = null
                    confPassLayoutEdt.error = "Passwords do not match."
                } else {

                    confPassLayoutEdt.error = null
                    val tag = this.localClassName
                    auth.createUserWithEmailAndPassword(emailTxt, passTxt)
                        .addOnCompleteListener(this) { task ->

                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(tag, "createUserWithEmail:success")
                                if (userType == "") {
                                    userTypeInputLayout.error = "Please select a user type"
                                }

                                val user = auth.currentUser

//                                userData=UserHelper(uid)
//
//                                val userData = hashMapOf(
//                                    "uid" to ,
//                                    "userType" to userType,
//                                    "name" to fullNameTxt,
//                                    "contact" to phoneTxt,
//                                    "email" to user?.email
//                                )


//                                val userData = when (userType) {
//                                    "A"->UserHelper(user?.uid.toString(),userType,fullNameTxt,phoneTxt,user?.email)
//                                    "C" -> userData["address"] = addressTxt
//                                    "D" -> userData["dob"] = dobTxt
//                                    else->""
//                                }
                                val currentTime= System.currentTimeMillis()
                                val userData = when (userType) {
                                    "A"->UserHelper(user?.uid.toString(),userType,fullNameTxt,phoneTxt,user?.email,false,currentTime)
                                    "C" -> UserHelper(user?.uid.toString(),userType,fullNameTxt,phoneTxt,user?.email,addressTxt,cityTxt,false,currentTime)
                                    "D" -> UserHelper(user?.uid.toString(),userType,fullNameTxt,phoneTxt,user?.email,cityTxt,false,dobTxt,currentTime)
                                    else->""
                                }

                                FirebaseDatabase
                                    .getInstance()
                                    .getReference("Users")
                                    .child(user?.uid.toString())
                                    .setValue(userData)
                                    .addOnSuccessListener {
                                        Log.d("Auth", "User Added Successfully")
                                        val dataMap = mutableMapOf<String, Any>()
                                        dataMap["total"] = ServerValue.increment(1)
                                        dataMap["A"]=ServerValue.increment(0)
                                        dataMap["C"]=ServerValue.increment(0)
                                        dataMap["D"]=ServerValue.increment(0)
                                        dataMap[userType] = ServerValue.increment(1)


                                        when(userType){
                                            "A"->{

                                                mEditor.putString("userType",userType)
                                                mEditor.apply()
                                                val intent = Intent(this, AdminHome::class.java)
                                                startActivity(intent)
                                            }
                                            "C"->{


                                                mEditor.putString("userType",userType)
                                                mEditor.apply()
                                                val intent = Intent(this, UserHome::class.java)
                                                startActivity(intent)


                                            }
                                            "D"->{


                                                mEditor.putString("userType",userType)
                                                mEditor.apply()
                                                val intent = Intent(this, DeliveryHome::class.java)
                                                startActivity(intent)

                                            }

                                        }
                                        FirebaseDatabase
                                            .getInstance()
                                            .getReference("UserStats")
                                            .updateChildren(dataMap)


                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Auth", "error", e)
                                    }


//                                db.collection("Users")
//                                    .document(user?.uid.toString())
//                                    .set(userData)
//                                    .addOnSuccessListener {
//                                        Log.d("Auth", "User Added Successfully")
//
//                                        when(userType){
//                                            "A"->{
//                                                val intent = Intent(this, AdminHome::class.java)
//                                                startActivity(intent)
//                                                mEditor.putString("userType",userType)
//                                            }
//                                            "C"->{
//                                                val intent = Intent(this, UserHome::class.java)
//                                                startActivity(intent)
//                                                mEditor.putString("userType",userType)
//
//                                            }
//                                            "D"->{
//                                                val intent = Intent(this, UserHome::class.java)
//                                                startActivity(intent)
//                                                mEditor.putString("userType",userType)
//                                            }
//
//                                        }
//
//                                    }
//                                    .addOnFailureListener { e ->
//                                        Log.e("Auth", "error", e)
//                                    }


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(tag, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                }

            }
        }


    }


