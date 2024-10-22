package com.rentatoy

import android.app.AlertDialog
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID


class UserViewToy : AppCompatActivity() {



    private lateinit var txtToyId: TextView
    private lateinit var txtToyName: TextView
    private lateinit var txtToyRent: TextView
    private lateinit var txtToyCategory: TextView
    private lateinit var txtToyDesc: TextView


    private var isAvailable: Boolean = true
    private lateinit var txtToyAgeGroup: TextView



    private lateinit var AddToCartBtn: Button
    private lateinit var toyImageView:ImageView


    private var toy:Toy?=null



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_view_toy)


        val toyID = intent.extras?.getString("toyID")


        AddToCartBtn=findViewById(R.id.btnAddToCartToy)
        toyImageView=findViewById(R.id.toyImageView)

        txtToyId = findViewById(R.id.txtToyID)
        txtToyName = findViewById(R.id.txtToyName)
        txtToyRent = findViewById(R.id.txtToyRent)
        txtToyCategory = findViewById(R.id.txtToyCategory)
        txtToyDesc = findViewById(R.id.txtToyDesc)
        txtToyAgeGroup=findViewById(R.id.txtToyAgeGroup)

        txtToyId.setText(toyID)
        Log.d("HHHH", "$toyID")
        setup(toyID)





        AddToCartBtn.setOnClickListener {
                    //Add item to firebase under user
                    //Cart fragment must fetch elements by their ids
                    //User clicks on the cart
                    // If toys are present then the user can checkout
                    //Checkout
                    //User Sets the number of daysa that he wants to rent for date from delivery date till the number of days chosen
                    //User pays for the same using the qr code
                    //Currently rented toys come under currently rented
                    //Once the rented time comes to an end the user gets a notification asking him to renew the order
                    //Renewal works the same way as an order but date starts from the day of renewal
                    //if not renewed then the delivery guy will visit to pickup the orders the next day automatically

                    //Admin sees and approves the orders by verifying the order id
                    //Order is assigned to delivery guy with same city as the customer



                val userId = FirebaseAuth.getInstance().currentUser?.uid

                if (userId != null) {
                    val cartRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Cart")
                    cartRef.child(toyID.toString()).setValue(true)
                        .addOnSuccessListener {
                            Log.d("Cart", "Item added to cart successfully")
                            // Handle success, if needed
                        }
                        .addOnFailureListener { e ->
                            Log.e("Cart", "Error adding item to cart: ${e.message}")
                            // Handle failure, if needed
                        }
                } else {
                    // Handle the case where the user is not authenticated
                    Log.e("Cart", "User not authenticated")
                }

        }



    }

















    private fun setup(toyId: String?) {


        FirebaseFirestore.getInstance()
            .collection("Toys")
            .document(toyId.toString())
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        toy = document.toObject()
                        txtToyName.text = toy?.toyName
                        txtToyRent.text = toy?.toyRent.toString()
                        txtToyCategory.text = toy?.toyCategory.toString()
                        //toyAvailInputLayout.helperText="Currently: ${toy?.isAvailable}"
                        txtToyAgeGroup.text=toy?.toyAgeGroup.toString()
                        txtToyDesc.text = toy?.toyDesc
                        Glide.with(this)
                            .load(toy?.toyImage)
                            .placeholder(R.drawable.alien)
                            .error(R.drawable.baseline_warning_24)
                            .into(toyImageView)
                    }
                }
            }
    }
}

















