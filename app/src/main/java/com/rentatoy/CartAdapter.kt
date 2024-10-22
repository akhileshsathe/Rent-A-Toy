package com.rentatoy

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(private val toyList: List<Toy>) : RecyclerView.Adapter<CartAdapter.ToyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cart_item, parent, false)
        return ToyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToyViewHolder, position: Int) {
        val toy = toyList[position]

        holder.bind(toy)

    }

    override fun getItemCount(): Int {
        return toyList.size
    }

    inner class ToyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val toyNameTextView: TextView = itemView.findViewById(R.id.toyNameTextView)

        private val toyRentTextView: TextView = itemView.findViewById(R.id.toyRentTextView)
        private val toyImageView: ImageView = itemView.findViewById(R.id.toyImageView)
        private val removeFromCartButton: Button = itemView.findViewById(R.id.btnRemoveFromCart)
        private val errorTxt: TextView = itemView.findViewById(R.id.txtError)


        fun bind(toy: Toy) {
            Log.d("Cart List", toy?.toyID.toString())
            toyNameTextView.text = toy.toyName

            toyRentTextView.text = "₹${toy.toyRent} / day"
            if(!toy.isAvailable){
                if (toy.isAvailable) {
                    val disabledColor = ContextCompat.getColor(itemView.context, R.color.disabled)
                    itemView.setBackgroundColor(disabledColor)
                    errorTxt.visibility=View.VISIBLE

                }


            }

            removeFromCartButton.setOnClickListener {
                removeItemFromCart(toy.toyID.toString())
            }

            // Load image using Glide
            Glide.with(itemView.context)
                .load(toy.toyImage)
                .placeholder(R.drawable.alien)
                .error(R.drawable.baseline_warning_24)
                .into(toyImageView)
        }
    }
    private fun removeItemFromCart(toyID: String) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val cartRef = database.getReference("Users").child(userId!!).child("Cart")

        // Remove the item from the cart in Realtime Database
        cartRef.child(toyID).removeValue()
    }



}

