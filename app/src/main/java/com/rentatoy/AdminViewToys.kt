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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.Locale


class AdminViewToys : AppCompatActivity() {

    private val toyList = mutableListOf<Toy>()
    private val filteredToyList = mutableListOf<Toy>()
    private lateinit var adapter: ToyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_toys)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ToyAdapter(filteredToyList)
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

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        // Assuming you have a collection named "toys"
        db.collection("Toys")
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
