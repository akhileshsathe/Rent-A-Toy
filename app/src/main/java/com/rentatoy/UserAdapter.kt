package com.rentatoy

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class UserAdapter(private val userList: List<UserHelper>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.bind(user)
        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, AdminUserDetails::class.java)
            // Pass any additional data to UserDetailsActivity if needed
            intent.putExtra("userId", user.uid)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.txtUserName)
        private val userEmailTextView: TextView = itemView.findViewById(R.id.txtUserEmail)
        private val userContactTextView: TextView = itemView.findViewById(R.id.txtUserContact)
        private val userCityTextView: TextView = itemView.findViewById(R.id.txtUserCity)
        private val userImage:ImageView=itemView.findViewById(R.id.userImageView)

        fun bind(user: UserHelper) {
            userNameTextView.text = user.name
            userEmailTextView.text = user.email
            userContactTextView.text=user.contact
            userCityTextView.text=user.city
            // You can bind more fields as needed
            val colorArray = listOf(
                "#FFB6C1", // LightPink
                "#FFC0CB", // Pink
                "#FFD700", // Gold
                "#FFE4B5", // Moccasin
                "#98FB98", // PaleGreen
                "#87CEEB", // SkyBlue
                "#ADD8E6", // LightBlue
                "#D8BFD8", // Thistle
                "#FFA07A", // LightSalmon
                "#FFDAB9", // PeachPuff
                "#E0FFFF", // LightCyan
                "#FF6347"  // Tomato
            )
//            val colorArray = listOf("#FFCDD2", "#BBDEFB", "#B2EBF2")
            userImage.setBackgroundColor(Color.parseColor(colorArray[Random.nextInt(colorArray.size)]))

        }
    }
}
