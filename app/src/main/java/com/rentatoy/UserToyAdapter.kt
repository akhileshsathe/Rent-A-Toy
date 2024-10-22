package com.rentatoy

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserToyAdapter(private val toyList: List<Toy>) : RecyclerView.Adapter<UserToyAdapter.ToyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_toy_item, parent, false)
        return ToyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToyViewHolder, position: Int) {
        val toy = toyList[position]

        holder.bind(toy)
        holder.itemView.setOnClickListener{view->
            val intent =Intent(view.context,UserViewToy::class.java)
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
            toyAgeGroupTextView.text = "Age:"+when(toy.toyAgeGroup){
                "Infants"->"0+"
                "Toddlers"->"1+"
                "Preschoolers"->"3+"
                "School-Age"->"6+"
                "Teens"->"13+"
                "Adults"->"20+"
                else -> {""}
            }
            toyRentTextView.text = toy.toyRent.toString()

            // Load image using Glide
            Glide.with(itemView.context)
                .load(toy.toyImage)
                .placeholder(R.drawable.alien)
                .error(R.drawable.baseline_warning_24)
                .into(toyImageView)
        }
    }



}

