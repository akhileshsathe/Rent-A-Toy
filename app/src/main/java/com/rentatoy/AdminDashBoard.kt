package com.rentatoy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.rentatoy.R

class AdminDashBoard : AppCompatActivity() {
    private lateinit var loadingProgressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dash_board)

        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        val txtTotalUserCount=findViewById<TextView>(R.id.txtTotalUserCount)
        val txtAdminCount=findViewById<TextView>(R.id.txtAdminCount)
        val txtCustomerCount=findViewById<TextView>(R.id.txtCustomerCount)
        val txtDeliveryPartnersCount=findViewById<TextView>(R.id.txtDeliveryPartnersCount)

        val txtToyCount: TextView = findViewById(R.id.txtToyCount)
        val txInfantsCount: TextView = findViewById(R.id.txInfantsCount)
        val txtToddlersCount: TextView = findViewById(R.id.txtToddlersount)
        val txtPreSchoolersCount: TextView = findViewById(R.id.txtPreSchoolersCount)
        val txtSchoolAgeCount: TextView = findViewById(R.id.txtSchoolAgeCount)
        val txtTeensCount: TextView = findViewById(R.id.txtTeensCount)
        val txtOrderCount: TextView = findViewById(R.id.txtOrderCount)
        val txtWeekCount: TextView = findViewById(R.id.txWeekCount)
        val txtMonthCount: TextView = findViewById(R.id.txtMonthCount)
        val txtYearCount: TextView = findViewById(R.id.txtYearCount)

        showLoading()

        FirebaseDatabase
            .getInstance()
            .getReference("UserStats")
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    // dataSnapshot contains the data from the "UserStats" node
                    val totalUsers = dataSnapshot.child("total").value
                    val admins = dataSnapshot.child("A").value
                    val customers = dataSnapshot.child("C").value
                    val deliveryPs = dataSnapshot.child("D").value
                    hideLoading()

                    // Do something with the values
                    if (totalUsers != null && admins != null && customers!=null && deliveryPs!=null) {
                        txtTotalUserCount.text=totalUsers.toString()
                        txtAdminCount.text=admins.toString()
                        txtCustomerCount.text=customers.toString()
                        txtDeliveryPartnersCount.text=deliveryPs.toString()
                        // Add more fields as needed
                    }
                } else {
                    println("No data exists at UserStats")
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures
                println("Error getting data: ${exception.message}")
            }
        showLoading()
        FirebaseDatabase
            .getInstance()
            .getReference("ToyStats")
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    // dataSnapshot contains the data from the "UserStats" node
                    val totalToys = dataSnapshot.child("total").value
                    val Infants = dataSnapshot.child("Infants").value
                    val Toddlers = dataSnapshot.child("Toddlers").value
                    val Preschoolers = dataSnapshot.child("Preschoolers").value
                    val School_Age = dataSnapshot.child("School-Age").value
                    val Teens = dataSnapshot.child("Teens").value
                    hideLoading()
                    // Do something with the values
                    if (totalToys != null && Infants != null && Toddlers!=null && Preschoolers!=null && School_Age!=null && Teens!=null) {
                        txtToyCount.text=totalToys.toString()
                        txInfantsCount.text=Infants.toString()
                        txtToddlersCount.text=Toddlers.toString()
                        txtPreSchoolersCount.text=Preschoolers.toString()
                        txtSchoolAgeCount.text=School_Age.toString()
                        txtTeensCount.text=Teens.toString()

                        // Add more fields as needed
                    }
                } else {
                    Log.d("","No data exists at UserStats")
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures
                println("Error getting data: ${exception.message}")
            }
        val databaseReference = FirebaseDatabase.getInstance().getReference("Orders")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the count of items in the node
                val count = dataSnapshot.childrenCount
                // Now 'count' contains the number of items in the node
                txtOrderCount.text= count.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                println("Error getting data: ${databaseError.message}")
            }
        })


        val databaseReference2 = FirebaseDatabase.getInstance().getReference("Orders")

// Get the current time in milliseconds
        val currentTimeMillis = System.currentTimeMillis()

// Calculate the start and end times for a specific period (e.g., one week, one month, one year)
        val oneWeekAgo = currentTimeMillis - (7 * 24 * 60 * 60 * 1000)

        val oneMonthAgo = currentTimeMillis - (30 * 24 * 60 * 60 * 1000)

        val oneYearAgo = currentTimeMillis - (365 * 24 * 60 * 60 * 1000)

        var count:Long
        // Function to count items based on a time range
        fun countItemsInTimeRange(startTime: Long, endTime: Long, callback: (Long) -> Unit) {
            databaseReference2.orderByChild("orderDate")
                .startAt(startTime.toDouble())
                .endAt(endTime.toDouble())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // Get the count of items in the specified time range
                        val count = dataSnapshot.childrenCount
                        callback(count)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors
                        println("Error getting data: ${databaseError.message}")
                        callback(-1) // Pass -1 as a count to indicate an error if needed
                    }
                })
        }

// Usage
        countItemsInTimeRange(oneWeekAgo, currentTimeMillis) { count ->
            txtWeekCount.text = count.toString()
        }

        countItemsInTimeRange(oneMonthAgo, currentTimeMillis) { count ->
            txtMonthCount.text = count.toString()
        }

        countItemsInTimeRange(oneYearAgo, currentTimeMillis) { count ->
            txtYearCount.text = count.toString()
        }

    }
    private fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }
    }


