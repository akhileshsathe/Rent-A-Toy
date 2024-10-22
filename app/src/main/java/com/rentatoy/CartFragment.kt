package com.rentatoy
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment() {

    private lateinit var cartRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var cartAdapter: CartAdapter
    private val cartItems: MutableList<String> = mutableListOf()
    private val cartToys=mutableListOf<Toy>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("CartFragment", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        // Initialize Firebase


        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)




        // Fetch cart items
        val database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        cartRef = database.getReference("Users").child(userId!!).child("Cart")
        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItems.clear()
                for (itemSnapshot in snapshot.children) {

                    val itemId = itemSnapshot.key
                    Log.d("Cart",itemId.toString())
                    if (itemId != null) {

                        cartItems.add(itemId)
                    }
                }
                fetchDataFromFirestore(cartItems)

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
        cartAdapter = CartAdapter(cartToys)
        recyclerView.adapter = cartAdapter

        return view
    }


    private fun fetchDataFromFirestore(ids: List<String>) {
        val db = FirebaseFirestore.getInstance()

        // Assuming you have a collection named "toys"
        db.collection("Toys")
            .whereIn("toyID", ids)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val toy = document.toObject(Toy::class.java)
                    cartToys.add(toy)
                    Log.d("Cart1",toy.toyName.toString())
                }
                cartAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("X", "Failed to fetch data: ${exception.message}")
                // Handle failure, e.g., log an error
            }
    }

}
