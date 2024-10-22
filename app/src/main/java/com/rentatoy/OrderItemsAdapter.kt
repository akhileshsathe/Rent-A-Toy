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

class OrderItemsAdapter(private val toyList: List<Toy>) : RecyclerView.Adapter<OrderItemsAdapter.ToyViewHolder>(){

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

            removeFromCartButton.visibility=View.GONE
            errorTxt.visibility=View.GONE
            // Load image using Glide
            Glide.with(itemView.context)
                .load(toy.toyImage)
                .placeholder(R.drawable.alien)
                .error(R.drawable.baseline_warning_24)
                .into(toyImageView)
        }
    }




}

