package com.rentatoy



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class Cart : AppCompatActivity() {

    private lateinit var adapter: CartAdapter
    private var cartItems = mutableListOf<String>()
    private var cartToys = mutableListOf<Toy>()
    private lateinit var checkoutBtn: Button
    private lateinit var totalTxt:Button
    private var canCheckOut:Boolean=true
    private var total:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        checkoutBtn=findViewById(R.id.btnCheckout)
        totalTxt=findViewById(R.id.txtTotal)
        checkoutBtn.setOnClickListener { checkout() }
        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(cartToys)
        recyclerView.adapter = adapter

        // Fetch data from Firestore
        fetchDataFromFirestore()

    }

    private fun fetchDataFromFirestore() {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val cartRef = database.getReference("Users").child(userId!!).child("Cart")

        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItems.clear()
                for (itemSnapshot in snapshot.children) {
                    val itemId = itemSnapshot.key
                    Log.d("Cart", itemId.toString())
                    if (itemId != null) {
                        cartItems.add(itemId)
                    }
                }
                fetchToys()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("Cart", "Failed to fetch cart items: ${error.message}")
            }
        })
    }
    private fun fetchToys() {
        val db = FirebaseFirestore.getInstance()

        // Assuming you have a collection named "toys"
        val toysCollection = db.collection("Toys")

        // Clear existing data in cartToys
        cartToys.clear()

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
                        if(!toy.isAvailable){
                            canCheckOut=false
                        }
                        total+=toy.toyRent
                        cartToys.add(toy)
                        Log.d("Cart1", toy.toyName.toString())
                    }
                }
                totalTxt.text="Total:₹$total"
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Cart", "Failed to fetch toys: ${exception.message}")
                // Handle failure, e.g., log an error
            }
    }

private fun checkout(){
    if (!canCheckOut){
        Toast.makeText(this, "Not all items from the cart are available anymore", Toast.LENGTH_SHORT).show()
    }
        val intent = Intent(this,Checkout::class.java)
        intent.putExtra("totalRent",total)
    val arrayList = ArrayList<String>(cartItems)
    intent.putStringArrayListExtra("cartItems",arrayList)
    startActivity(intent)
}
}
