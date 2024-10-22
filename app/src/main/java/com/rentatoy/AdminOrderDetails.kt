package com.rentatoy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AdminOrderDetails : AppCompatActivity() {

    private lateinit var txtOrderDueDate: TextView
    private lateinit var toggleSwitch: SwitchCompat
    private lateinit var adapter: OrderItemsAdapter
    private var order: OrderHelper = OrderHelper()
    private lateinit var txtUserId: TextView
    private lateinit var txtAddress: TextView
    private lateinit var txtCity: TextView
    private lateinit var txtEmail: TextView
    private  lateinit var verifyPayment: LinearLayout
    private lateinit var txtOrderDate: TextView
    private lateinit var txtOrderDeliveryDate: TextView
    private lateinit var txtOrderReturnDate: TextView
    private lateinit var txtUpiRef: TextView
    private lateinit var txtOrderAmount: TextView
    private lateinit var txtOrderDuration: TextView
    private lateinit var txtOrderStatus: TextView
    private lateinit var txtContact:TextView
    private var cartToys = mutableListOf<Toy>()
    private var orderKey:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_order_details)
        orderKey=intent.getStringExtra("orderKey").toString()
        toggleSwitch = findViewById(R.id.toggleSwitch)
        txtUserId = findViewById(R.id.txtUserId)
        txtAddress = findViewById(R.id.txtAddress)
        txtCity = findViewById(R.id.txtCity)
        verifyPayment=findViewById(R.id.verifyPayment)
        txtEmail = findViewById(R.id.txtEmail)
        txtOrderDate = findViewById(R.id.txtOrderDate)
        txtOrderDeliveryDate = findViewById(R.id.txtOrderDeliveryDate)
        txtOrderReturnDate = findViewById(R.id.txtOrderReturnDate)
        txtOrderDueDate = findViewById(R.id.txtOrderDueDate)

        txtUpiRef = findViewById(R.id.txtUpiRef)
        txtOrderAmount = findViewById(R.id.txtOrderAmount)
        txtOrderDuration = findViewById(R.id.txtOrderDuration)
        txtOrderStatus = findViewById(R.id.orderStatus)
        txtContact=findViewById(R.id.txtContact)




        setup(orderKey)

        // Set an initial state


        // Set a listener to handle switch changes
        toggleSwitch.setOnCheckedChangeListener { _, isChecked ->

                markOrderAsApproved(order.orderKey, isChecked)
        }


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = OrderItemsAdapter(cartToys)
        recyclerView.adapter = adapter
    }
    private fun setup(orderKey: String?) {

        FirebaseDatabase.getInstance()
            .getReference("Orders")
            .child(orderKey.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        order = snapshot.getValue(OrderHelper::class.java)!!

                        toggleSwitch.isChecked = order.isApproved



                        fetchToys(order.cartItems)

                        txtUserId.text = order.userId
                        txtAddress.text = order.address
                        txtCity.text = order.city.toString()
                        txtEmail.text = order.email
                        txtContact.text=order.contact
                        if(order.isDelivered) {
                            txtOrderDeliveryDate.text =
                                convertEpochToDate(order.orderDeliveryDate ?: 0)
                            verifyPayment.visibility= View.GONE
                            txtOrderStatus.visibility=View.VISIBLE

                            txtOrderStatus.text=if(order.isReturned) "Returned" else "Delivered"
                            txtOrderReturnDate.text =
                                convertEpochToDate(order.orderReturnDate ?: 0)
                            val DueDate=addDaysToEpochDate(order.orderDeliveryDate.toString().toLong(),order.duration.toInt())
                            txtOrderDueDate.text=DueDate.toString()
                        }
                        txtOrderDate.text = convertEpochToDate(order.orderDate ?: 0)


                        txtUpiRef.text = order.upiRef
                        txtOrderAmount.text = "Order Amount: ${order.orderAmount}"
                        txtOrderDuration.text = "Duration: ${order.duration} days"



                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
    fun convertEpochToDate(epochTime: Long): String {
        return try {
            val date = Date(epochTime)


            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())


            dateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


    fun markOrderAsApproved(orderKey: String,value:Boolean) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(orderKey)

        // Update isApproved to true
        databaseReference.child("isApproved").setValue(value)
    }

    private fun fetchToys(cartItems:ArrayList<String>) {
        val db = FirebaseFirestore.getInstance()


        val toysCollection = db.collection("Toys")


        cartToys.clear()


        val tasks: MutableList<Task<DocumentSnapshot>> = mutableListOf()


        for (itemId in cartItems) {
            val toyDocument = toysCollection.document(itemId)
            tasks.add(toyDocument.get())
        }


        Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val toy = document.toObject(Toy::class.java)
                    if (toy != null) {
                        cartToys.add(toy)
                        Log.d("Cart1", toy.toyName.toString())
                    }
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("Cart", "Failed to fetch toys: ${exception.message}")

            }
    }
    fun addDaysToEpochDate(epochDate: Long, daysToAdd: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = epochDate
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)
        return calendar.timeInMillis
    }
}