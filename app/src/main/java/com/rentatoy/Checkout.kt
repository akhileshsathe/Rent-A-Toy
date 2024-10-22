package com.rentatoy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class Checkout : AppCompatActivity() {
    private lateinit var grandTotaltxt: TextView
    private lateinit var itemTotaltxt: TextView
    private var rent: Long = 0
    private lateinit var durationLayoutEdt: TextInputLayout
    private lateinit var durationEdt: EditText
    private lateinit var UpiRefLayoutEdt: TextInputLayout
    private lateinit var UpiRefEdt: EditText
    private lateinit var durationBtn: Button
    private lateinit var confirmBtn: Button
    private var grandTotal:Long=0
    private lateinit var totalTxt: TextView
    private lateinit var UpiRefTxt: TextView
    private lateinit var upiQRImg: ImageView
    private lateinit var linearLayout: LinearLayout
    private lateinit var cartItems: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        rent = intent.getLongExtra("totalRent", 100)
        cartItems = intent.getStringArrayListExtra("cartItems")!!

        upiQRImg = findViewById(R.id.upiQRImg)
        durationEdt = findViewById(R.id.edtDuration)
        durationLayoutEdt = findViewById(R.id.edtDurationLayout)
        UpiRefEdt = findViewById(R.id.edtUpiRef)
        UpiRefLayoutEdt = findViewById(R.id.edtUpiRefLayout)
        durationBtn = findViewById(R.id.btnDuration)
        confirmBtn = findViewById(R.id.btnConfirm)
        totalTxt = findViewById(R.id.txtTotal)
        itemTotaltxt = findViewById(R.id.txtItemTotal)
        grandTotaltxt = findViewById(R.id.txtGrandTotal)
        UpiRefTxt = findViewById(R.id.txtUpiRef)
        linearLayout = findViewById(R.id.linearLayout1)

        durationBtn.setOnClickListener {
            if (durationEdt.text.isEmpty()) {
                durationLayoutEdt.error = "* Required"
            } else {
                durationLayoutEdt.error = null
                val duration = durationEdt.text.toString().toLong()


                val totalAmount = duration * rent
                grandTotal = totalAmount + 10

                itemTotaltxt.text = "$rent*$duration = $totalAmount"
                grandTotaltxt.text = "₹$grandTotal"
                totalTxt.text = "Amount: ₹$grandTotal"
                linearLayout.visibility = View.VISIBLE


            }

        }
        confirmBtn.setOnClickListener {

            val UpiRef = UpiRefEdt.text.toString()
            if (UpiRef.isEmpty()) {
                Toast.makeText(this,UpiRef , Toast.LENGTH_SHORT).show()
                UpiRefLayoutEdt.error = "* Required"
            } else {
                UpiRefLayoutEdt.error = null
                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                val ordersRef: DatabaseReference = database.getReference("Orders")
                val orderKey = ordersRef.push().key
                val auth = FirebaseAuth.getInstance()
                val userId = auth.currentUser?.uid.toString()

                val databaseReference: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)

                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // Check if the user exists
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(UserHelper::class.java)
                            val address = user?.address.toString()
                            val city=user?.city.toString()
                            val email=user?.email
                            val contact=user?.contact

                            val currentTime= System.currentTimeMillis()
                            val order = OrderHelper(orderKey,userId
                                , address,city,cartItems,email
                                ,false,false,false
                                ,currentTime,0,0,UpiRef
                                ,grandTotal,durationEdt.text.toString().toLong()
                                ,contact
                            )
                            //mark the toys as unavailable
                            //order
                            //userid
                            //user email
                            //order id
                            //order item id
                            //order isApproved
                            //order isDelivered
                            //order isReturned
                            //order date
                            //usercity
                            //order rental date
                            //order return date
                            //upiref
                            FirebaseDatabase
                                .getInstance()
                                .getReference("Orders")
                                .child(orderKey.toString())
                                .setValue(order)
                                .addOnSuccessListener {
                                    unavailToys()
                                    val cartRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Cart")

                                    cartRef.removeValue()
                                        .addOnSuccessListener {
                                            // Deletion successful
                                            Toast.makeText(baseContext, "Order placed successfully", Toast.LENGTH_SHORT)
                                                .show()
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            // Handle the error
                                        }

                                }


                        }
                    }


                    override fun onCancelled(error: DatabaseError) {
                        // Handle the error, e.g., log an error message

                    }
                })

            }
        }
    }


        private fun unavailToys() {
            val db = FirebaseFirestore.getInstance()

            // Assuming you have a collection named "toys"
            val toysCollection = db.collection("Toys")

            // Use a batch write to update multiple documents atomically
            val batch = db.batch()

            // Create a list to store tasks
            val tasks: MutableList<Task<DocumentSnapshot>> = mutableListOf()

            // Create tasks for each item ID
            for (itemId in cartItems) {
                val toyDocument = toysCollection.document(itemId)
                tasks.add(toyDocument.get())
            }

            // Use Tasks.whenAllSuccess to handle multiple tasks
            Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val toy = document.toObject(Toy::class.java)
                        if (toy != null) {
                            // Update the isAvailable property to false
                            batch.update(
                                toysCollection.document(toy.toyID.toString()),
                                "isAvailable",
                                false
                            )
                        }
                    }

                    // Commit the batch write to update multiple documents
                    batch.commit()

                }
                .addOnFailureListener { exception ->
                    Log.e("Cart", "Failed to fetch toys: ${exception.message}")
                    // Handle failure, e.g., log an error
                }
        }

    }
