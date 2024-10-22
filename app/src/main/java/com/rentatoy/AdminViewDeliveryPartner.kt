package com.rentatoy

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.Locale


class AdminViewDeliveryPartner : AppCompatActivity() {

    private val userList = mutableListOf<UserHelper>()
    private val filteredUserList = mutableListOf<UserHelper>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_delivery_partner)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(filteredUserList)
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
                filterUsers(newText)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        if(filteredUserList.isEmpty()){

        }
    }

    private fun fetchDataFromRealtimeDatabase() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        val query = databaseReference.orderByChild("userType").equalTo("D")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                filteredUserList.clear()

                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(UserHelper::class.java)
                    user?.let {
                        userList.add(it)
                        filteredUserList.add(it)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("X", "v")
                // Handle failure, e.g., log an error
            }
        })
    }

    private fun filterUsers(query: String?) {
        filteredUserList.clear()
        if (query.isNullOrBlank()) {
            filteredUserList.addAll(userList)
        } else {
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            userList.forEach { user ->
                if (user.name.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    user.email.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    user.contact.lowercase(Locale.getDefault()).contains(lowerCaseQuery)||
                    user.city.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
                ) {
                    filteredUserList.add(user)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_infants -> filterToysByAgeGroup("Infants")
            R.id.action_filter_toddlers -> filterToysByAgeGroup("Toddlers")
            // Add other filter options
        }
        return true
    }

    private fun filterToysByAgeGroup(name: String) {
        filteredUserList.clear()
        userList.forEach { user ->
            if (user.name == name) {
                filteredUserList.add(user)
            }
        }
        adapter.notifyDataSetChanged()
    }




}
