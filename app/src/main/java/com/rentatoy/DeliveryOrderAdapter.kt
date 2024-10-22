package com.rentatoy
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DeliveryOrderAdapter(private val orderList: List<OrderHelper>) : RecyclerView.Adapter<DeliveryOrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        holder.bind(order)
        holder.itemView.setOnClickListener { view ->
            if(!order.isDelivered){
                val intent = Intent(view.context, DeliveryScreen::class.java)
                intent.putExtra("orderKey", order.orderKey)
                intent.putExtra("isDelivered",order.isDelivered)
                view.context.startActivity(intent)
            }
            else if(!order.isReturned){
                val intent = Intent(view.context, ReturnScreen::class.java)
                intent.putExtra("orderKey", order.orderKey)
                intent.putExtra("isDelivered",order.isDelivered)
                view.context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //        private val userIdTextView: TextView = itemView.findViewById(R.id.userIdTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val cityTextView: TextView = itemView.findViewById(R.id.cityTextView)
        private val cartItemsTextView: TextView = itemView.findViewById(R.id.cartItemsTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        //        private val isApprovedTextView: TextView = itemView.findViewById(R.id.isApprovedTextView)
//        private val isDeliveredTextView: TextView = itemView.findViewById(R.id.isDeliveredTextView)
//        private val isReturnedTextView: TextView = itemView.findViewById(R.id.isReturnedTextView)
        private val orderDateTextView: TextView = itemView.findViewById(R.id.orderDateTextView)
        private val orderDeliveryDateTextView: TextView = itemView.findViewById(R.id.orderDeliveryDateTextView)
        private val orderReturnDateTextView: TextView = itemView.findViewById(R.id.orderReturnDateTextView)
        private val upiRefTextView: TextView = itemView.findViewById(R.id.upiRefTextView)
        private val orderAmountTextView: TextView = itemView.findViewById(R.id.orderAmountTextView)
        private val orderDurationTextView: TextView = itemView.findViewById(R.id.orderDurationTextView)
        fun bind(order: OrderHelper) {

//            userIdTextView.text = "User ID: ${order.userId}"
            addressTextView.text = "${order.address}"
            cityTextView.text = "${order.city}"
            cartItemsTextView.text = "${order.cartItems}"
            emailTextView.text = "${order.email}"
//            isApprovedTextView.text = "Is Approved: ${order.isApproved}"
//            isDeliveredTextView.text = "Is Delivered: ${order.isDelivered}"
//            isReturnedTextView.text = "Is Returned: ${order.isReturned}"


            orderDateTextView.text = "${convertEpochToDate(order.orderDate)}"

//            if(order.isDelivered){
//                orderDeliveryDateTextView.visibility=View.VISIBLE
//                orderDeliveryDateTextView.text = "${convertEpochToDate(order.orderDeliveryDate)}"
//            }
//            if(order.isReturned){
//                orderReturnDateTextView.visibility=View.VISIBLE
//                orderReturnDateTextView.text = "${order.orderReturnDate}"
//            }



            upiRefTextView.text = "${order.upiRef}"
            orderAmountTextView.text = "₹${order.orderAmount}"
            orderDurationTextView.text = "${order.duration} days"
        }
    }



    fun convertEpochToDate(epochTime: Long): String {
        try {
            // Convert epoch time to Date object
            val date = Date(epochTime)

            // Define the desired date format
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

            // Format the Date object to a string
            return dateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

}
