package com.rentatoy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import java.util.Locale


class UserHome : AppCompatActivity() {

    private val toyList = mutableListOf<Toy>()
    private val filteredToyList = mutableListOf<Toy>()
    private lateinit var adapter: UserToyAdapter
    private lateinit var btnUserSettings: Button
    private lateinit var btnCart:Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)
        btnUserSettings=findViewById(R.id.btnUserSettings)
        btnUserSettings.width=btnUserSettings.height
        auth = Firebase.auth

        btnUserSettings.setOnClickListener {

            val intent = Intent(this, UserMenu::class.java)

            startActivity(intent)

        }

        btnCart=findViewById(R.id.btnCart)
        btnCart.setOnClickListener {
            val intent =Intent(this,Cart::class.java)
            startActivity(intent)
        }
        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        adapter = UserToyAdapter(filteredToyList)
        recyclerView.adapter = adapter

        // Fetch data from Firestore
        fetchDataFromFirestore()

        // Set up search functionality
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterToys(newText)
                return true
            }
        })
    }

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        // Assuming you have a collection named "toys"
        db.collection("Toys")
            .whereEqualTo("isAvailable",true)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val toy = document.toObject(Toy::class.java)
                    toyList.add(toy)
                    filteredToyList.add(toy)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("X","v")
                // Handle failure, e.g., log an error
            }
    }
    private fun filterToys(query: String?) {
        filteredToyList.clear()
        if (query.isNullOrBlank()) {
            filteredToyList.addAll(toyList)
        } else {
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            toyList.forEach { toy ->
                if (toy.toyName.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    toy.toyCategory.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    toy.toyAgeGroup.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
                ) {
                    filteredToyList.add(toy)
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

    private fun filterToysByAgeGroup(ageGroup: String) {
        filteredToyList.clear()
        toyList.forEach { toy ->
            if (toy.toyAgeGroup == ageGroup) {
                filteredToyList.add(toy)
            }
        }
        adapter.notifyDataSetChanged()
    }




}
