// AdminViewOrders.kt
package com.rentatoy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DeliveryHistory : AppCompatActivity() {
    private var city: String=""
    private lateinit var loadingProgressBar: ProgressBar
    private val orderList = mutableListOf<OrderHelper>() // Use your OrderHelper class
    private val filteredOrderList = mutableListOf<OrderHelper>() // Use your OrderHelper class
    private lateinit var adapter: DeliveryOrderAdapter // Create an OrderAdapter class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_delivered)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        setup()
        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DeliveryOrderAdapter(filteredOrderList) // Create an OrderAdapter instance
        recyclerView.adapter = adapter

        // Fetch data from Firestore
        fetchDataFromRealtimeDatabase()

        // Set up search functionality
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterOrders(newText)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun fetchDataFromRealtimeDatabase() {
        val database = FirebaseDatabase.getInstance()
        val ordersRef = database.getReference("Orders")


        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val order = snapshot.getValue(OrderHelper::class.java) // Use your OrderHelper class
                    if (order != null && order.isApproved) {


                            orderList.add(order)
                            filteredOrderList.add(order)


                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("X", "Failed to fetch orders: ${databaseError.message}")
                // Handle failure, e.g., log an error
            }
        })
    }


    private fun filterOrders(query: String?) {
        filteredOrderList.clear()
        if (query.isNullOrBlank()) {
            filteredOrderList.addAll(orderList)
        } else {
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            orderList.forEach { order ->


                    // Add your filtering logic based on order attributes
                    if (order.userId.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        order.address.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        order.email.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
                    ) {
                        if(order.isApproved){
                        filteredOrderList.add(order)}
                    }

            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun setup() {
        val userID = Firebase.auth.currentUser?.uid.toString()
        showLoading()

        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue(UserHelper::class.java)

                if (userData != null) {
                    hideLoading()
                    city = userData.city.lowercase()
                } else {
                    Log.d("Auth", "User Data is null")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Auth", "Error retrieving user data: ${databaseError.message}")
            }
        })
        hideLoading()
    }

    private fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

    fun isDateGreaterOrEqual(epochTime1: Long, epochTime2: Long): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date1 = Date(epochTime1 * 1000)
        val date2 = Date(epochTime2 * 1000)

        val formattedDate1 = dateFormat.format(date1)
        val formattedDate2 = dateFormat.format(date2)

        return formattedDate1 <= formattedDate2
    }
}

