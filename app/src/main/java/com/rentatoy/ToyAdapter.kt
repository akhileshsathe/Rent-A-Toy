package com.rentatoy

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class ToyAdapter(private val toyList: List<Toy>) : RecyclerView.Adapter<ToyAdapter.ToyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.toy_item, parent, false)
        return ToyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToyViewHolder, position: Int) {
        val toy = toyList[position]

        holder.bind(toy)
        holder.itemView.setOnClickListener{view->
            val intent =Intent(view.context,AdminEditToy::class.java)
            intent.putExtra("toyID",toy.toyID.toString())
            view.context.startActivity( intent)
        }
    }

    override fun getItemCount(): Int {
        return toyList.size
    }

    inner class ToyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val toyNameTextView: TextView = itemView.findViewById(R.id.toyNameTextView)
        private val toyCategoryTextView: TextView = itemView.findViewById(R.id.toyCategoryTextView)
        private val toyAgeGroupTextView: TextView = itemView.findViewById(R.id.toyAgeGroupTextView)
        private val toyRentTextView: TextView = itemView.findViewById(R.id.toyRentTextView)
        private val toyImageView: ImageView = itemView.findViewById(R.id.toyImageView)


        fun bind(toy: Toy) {
            toyNameTextView.text = toy.toyName
            toyCategoryTextView.text = toy.toyCategory
            toyAgeGroupTextView.text = toy.toyAgeGroup
            toyRentTextView.text = "₹${toy.toyRent} / day"

            // Load image using Glide
            Glide.with(itemView.context)
                .load(toy.toyImage)
                .placeholder(R.drawable.alien)
                .error(R.drawable.baseline_warning_24)
                .into(toyImageView)
        }
    }



}

