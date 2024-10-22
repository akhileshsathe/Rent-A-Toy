package com.rentatoy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar


class CustomerProfile : AppCompatActivity() {
    private lateinit var loadingProgressBar: ProgressBar
    private var userType:String=""
    private lateinit var EditBtn: TextView
    private lateinit var email: EditText
    private lateinit var fullNameEdt: EditText
    private lateinit var phoneEdt: EditText
    private lateinit var addressEdt: EditText
    private lateinit var dobEdt: EditText
    private lateinit var cityEdt:EditText
    private lateinit var userTypeTxt:TextView




    private lateinit var auth: FirebaseAuth




    private lateinit var fullNameLayoutEdt: TextInputLayout
    private lateinit var emailLayoutEdt: TextInputLayout
    private lateinit var phoneLayoutEdt: TextInputLayout

    private lateinit var addressLayoutEdt: TextInputLayout
    private lateinit var cityLayoutEdt: TextInputLayout


    private lateinit var dobLayoutEdt: TextInputLayout
    private var userRegTime:Long=0







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile)

        //Firebase
        auth = Firebase.auth

        //EditText
        email = findViewById(R.id.edtEmail)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        fullNameEdt = findViewById(R.id.edtFullName)
        phoneEdt = findViewById(R.id.edtPhone)
        addressEdt = findViewById(R.id.edtAddress)
        fullNameEdt = findViewById(R.id.edtFullName)
        userTypeTxt=findViewById(R.id.txtUserType)

        //buttons
        EditBtn = findViewById(R.id.txtEditBtn)
        dobEdt = findViewById(R.id.edtDOB)
        cityEdt = findViewById(R.id.edtCity)

        fullNameLayoutEdt = findViewById(R.id.edtFullNameLayout)


        emailLayoutEdt = findViewById(R.id.edtEmailLayout)
        phoneLayoutEdt = findViewById(R.id.edtPhoneLayout)
        addressLayoutEdt = findViewById(R.id.edtAddressLayout)
        cityLayoutEdt = findViewById(R.id.edtCityLayout)
        dobLayoutEdt = findViewById(R.id.edtDOBLayout)


        //Spinner


        dobLayoutEdt.visibility = View.GONE
        addressLayoutEdt.visibility = View.GONE
        cityLayoutEdt.visibility = View.GONE

        setup(auth.currentUser?.uid.toString())


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
        var edtBtnMode = true
        EditBtn.setOnClickListener {

            if (edtBtnMode) {
                edtBtnMode = false
                fullNameEdt.isEnabled=true
                phoneEdt.isEnabled=true
                dobEdt.isEnabled=true
                cityEdt.isEnabled=true
                addressEdt.isEnabled=true

                EditBtn.text = "Save changes"
            } else {
                edtBtnMode = false
                fullNameEdt.isEnabled=false
                phoneEdt.isEnabled=false
                dobEdt.isEnabled=false
                cityEdt.isEnabled=false
                addressEdt.isEnabled=false


                EditBtn.text = "Edit"

//            Toast.makeText(baseContext, "Signup button pressed", Toast.LENGTH_SHORT).show()
                val emailTxt = email.text.toString()

                val fullNameTxt = fullNameEdt.text.toString()
                val phoneTxt = phoneEdt.text.toString()
                val addressTxt = addressEdt.text.toString()
                val dobTxt = dobEdt.text.toString()
                val cityTxt = cityEdt.text.toString()


                if (userType == "") {
                    Toast.makeText(this, "User Type is required", Toast.LENGTH_SHORT).show()
                } else if (fullNameTxt.isEmpty()) {

                    fullNameLayoutEdt.error = "Required *"
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
                    fullNameLayoutEdt.error = null

                } else if (phoneTxt.isEmpty()) {
                    emailLayoutEdt.error = null
                    phoneLayoutEdt.error = "Required *"

                } else if (phoneTxt.length < 10 || phoneTxt.length > 10) {
                    emailLayoutEdt.error = null
                    phoneLayoutEdt.error = "Invalid phone"
                } else if (userType == "C" && addressTxt.isEmpty()) {
                    phoneLayoutEdt.error = null
                    addressLayoutEdt.error = "Required *"

                } else if (userType == "D" && dobTxt.isEmpty()) {
                    phoneLayoutEdt.error = null
                    dobLayoutEdt.error = "Required *"

                } else if ((userType == "C" || userType == "D") && cityTxt.isEmpty()) {
                    addressLayoutEdt.error = null
                    phoneLayoutEdt.error = null
                    dobLayoutEdt.error = null
                    cityLayoutEdt.error = "Required *"
                } else {
                    cityLayoutEdt.error = null
                    addressLayoutEdt.error = null
                    phoneLayoutEdt.error = null
                    dobLayoutEdt.error = null

                    val tag = this.localClassName


                    val user = auth.currentUser

//
                    val currentTime = System.currentTimeMillis()
                    val userData = when (userType) {
                        "A" -> UserHelper(
                            user?.uid.toString(),
                            userType,
                            fullNameTxt,
                            phoneTxt,
                            user?.email,
                            false,
                            userRegTime
                        )

                        "C" -> UserHelper(
                            user?.uid.toString(),
                            userType,
                            fullNameTxt,
                            phoneTxt,
                            user?.email,
                            addressTxt,
                            cityTxt,
                            false,
                            userRegTime
                        )

                        "D" -> UserHelper(
                            user?.uid.toString(),
                            userType,
                            fullNameTxt,
                            phoneTxt,
                            user?.email,
                            cityTxt,
                            false,
                            dobTxt,
                            userRegTime
                        )

                        else -> ""
                    }

                    FirebaseDatabase
                        .getInstance()
                        .getReference("Users")
                        .child(user?.uid.toString())
                        .setValue(userData)
                        .addOnSuccessListener {
                            Log.d("Auth", "User Added Successfully")


                        }
                        .addOnFailureListener { e ->
                            Log.e("Auth", "error", e)
                        }


                }
            }

        }
    }

    private fun setup(userID:String) {
        showLoading()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Retrieve data from dataSnapshot
                val userData = dataSnapshot.getValue(UserHelper::class.java)

                // Now you can use userData for further processing or display
                if (userData != null) {
                   userType=userData.userType

                    fullNameEdt.setText(userData.name)
                    email.setText(userData.email)
                    phoneEdt.setText(userData.contact)
                    userRegTime=userData.registedOn

                    hideLoading()
                    when(userType){
                        "A"->{
                            userTypeTxt.text="Admin"
                            dobLayoutEdt.visibility=View.GONE
                            addressLayoutEdt.visibility=View.GONE
                            cityLayoutEdt.visibility=View.GONE

                        }
                        "C"->{
                            userTypeTxt.text="Customer"
                            dobLayoutEdt.visibility=View.GONE
                            addressLayoutEdt.visibility=View.VISIBLE
                            cityLayoutEdt.visibility=View.VISIBLE
                            addressEdt.setText(userData.address)
                            cityEdt.setText(userData.city)
                        }
                        "D"->{
                            userTypeTxt.text="Delivery Partner"
                            dobLayoutEdt.visibility=View.VISIBLE
                            addressLayoutEdt.visibility=View.VISIBLE
                            cityLayoutEdt.visibility=View.VISIBLE
                            addressEdt.setText(userData.address)
                            cityEdt.setText(userData.city)





                            dobEdt.setText( userData.dob.toString())
                        }

                    }

                } else {
                    Log.d("Auth", "User Data is null")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur during the data retrieval
                Log.e("Auth", "Error retrieving user data: ${databaseError.message}")
            }
        })
    }
    private fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE

        fullNameLayoutEdt.visibility = View.GONE


        emailLayoutEdt.visibility = View.GONE
        phoneLayoutEdt.visibility = View.GONE
        addressLayoutEdt.visibility = View.GONE
        cityLayoutEdt.visibility = View.GONE
        dobLayoutEdt.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
        fullNameLayoutEdt.visibility = View.VISIBLE


        emailLayoutEdt.visibility = View.VISIBLE
        phoneLayoutEdt.visibility = View.VISIBLE
        addressLayoutEdt.visibility = View.VISIBLE
        cityLayoutEdt.visibility = View.VISIBLE
        dobLayoutEdt.visibility = View.VISIBLE
    }

}




