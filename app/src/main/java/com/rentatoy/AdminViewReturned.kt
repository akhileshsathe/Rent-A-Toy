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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class AdminViewReturned : AppCompatActivity() {

    private val orderList = mutableListOf<OrderHelper>()
    private val filteredOrderList = mutableListOf<OrderHelper>()
    private lateinit var adapter: AdminOrderAdapter
    private lateinit var loadingProgressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_delivered)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AdminOrderAdapter(filteredOrderList) // Create an OrderAdapter instance
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
        showLoading()
        val database = FirebaseDatabase.getInstance()
        val ordersRef = database.getReference("Orders")


        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val order = snapshot.getValue(OrderHelper::class.java) // Use your OrderHelper class
                    if (order != null) {
                        if(order.isDelivered && order.isReturned){
                            orderList.add(order)
                            filteredOrderList.add(order)
                        }

                    }
                }
                adapter.notifyDataSetChanged()
                hideLoading()
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
                if(order.isDelivered && order.isReturned) {
                    // Add your filtering logic based on order attributes
                    if (order.userId.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        order.address.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        order.email.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
                    ) {
                        filteredOrderList.add(order)
                    }
                }
            }
        }
        adapter.notifyDataSetChanged()
    }
    private fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

}

