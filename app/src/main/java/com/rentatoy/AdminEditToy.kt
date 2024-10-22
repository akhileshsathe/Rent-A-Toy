package com.rentatoy

import android.app.AlertDialog
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID


class AdminEditToy : AppCompatActivity() {


    private lateinit var edtToyIdLayout: TextInputLayout
    private lateinit var edtToyNameLayout: TextInputLayout
    private lateinit var edtToyRentalLayout: TextInputLayout
    private lateinit var edtToyCategoryLayout: TextInputLayout
    private lateinit var edtToyDescLayout: TextInputLayout
    private lateinit var edtToyId: EditText
    private lateinit var edtToyName: EditText
    private lateinit var edtToyRent: EditText
    private lateinit var edtToyCategory: EditText
    private lateinit var edtToyDesc: EditText


    private var isAvailable: Boolean = true
    private lateinit var toyAvailSpinner: AutoCompleteTextView
    private lateinit var toyAvailInputLayout: TextInputLayout
    private var toyAgeGroup: String = ""
    private lateinit var toyAgeGroupSpinner: AutoCompleteTextView
    private lateinit var toyAgeGroupInputLayout: TextInputLayout

    private lateinit var addImageBtn: Button
    private lateinit var editToyBtn: Button
    private lateinit var deleteToyBtn:Button
    private val compressQuality = 50 // Adjust as needed
    private var selectedImage: ByteArray? = null
    private var toy:Toy?=null
    private var isImageChanged:Boolean=false


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_toy)
        //val toyID=intent.getStringExtra("toyId")

        val toyID = intent.extras?.getString("toyID")
        toyAvailSpinner = findViewById(R.id.spinnerToyAvailable)
        toyAvailInputLayout = findViewById(R.id.edtToyAvailableLayout)
        toyAgeGroupSpinner = findViewById(R.id.spinnerAgeGroupAvailable)
        toyAgeGroupInputLayout = findViewById(R.id.edtToyAgeGroupLayout)
        addImageBtn = findViewById(R.id.btnAddImage)
        editToyBtn = findViewById(R.id.btnEditToy)
        deleteToyBtn=findViewById(R.id.btnDeleteToy)


        edtToyIdLayout = findViewById(R.id.edtToyIDLayout)
        edtToyNameLayout = findViewById(R.id.edtToyNameLayout)
        edtToyRentalLayout = findViewById(R.id.edtToyRentalLayout)
        edtToyCategoryLayout = findViewById(R.id.edtToyCategoryLayout)
        edtToyDescLayout = findViewById(R.id.edtToyDescLayout)
        edtToyId = findViewById(R.id.edtToyID)
        edtToyName = findViewById(R.id.edtToyName)
        edtToyRent = findViewById(R.id.edtToyRent)
        edtToyCategory = findViewById(R.id.edtToyCategory)
        edtToyDesc = findViewById(R.id.edtToyDesc)

        edtToyId.setText(toyID)
        Log.d("HHHH", "$toyID")
        setup(toyID)

        val optionsList = arrayOf("True", "False")


        val optionsArrayAdapter = ArrayAdapter(this, R.layout.layout_spinner_item, optionsList)
        toyAvailSpinner.setAdapter(optionsArrayAdapter)

        toyAvailSpinner.setOnItemClickListener { parent, _, position, _ ->

            //Toast.makeText(baseContext, item, Toast.LENGTH_SHORT).show()
            isAvailable = parent.getItemAtPosition(position).toString() == "True"


        }

        toyAvailInputLayout.setEndIconOnClickListener {
            toyAvailSpinner.showDropDown()
        }

        val ageGroupList = arrayOf(
            "Infants [0-12 months]",
            "Toddlers [1-3 years]",
            "Preschoolers [3-5 years]",
            "School-Age [6-12 years] ",
            "Teens [13-19 year]"
        )


        val ageGroupArrayAdapter = ArrayAdapter(this, R.layout.layout_spinner_item, ageGroupList)
        toyAgeGroupSpinner.setAdapter(ageGroupArrayAdapter)

        toyAgeGroupSpinner.setOnItemClickListener { parent, _, position, _ ->

            //Toast.makeText(baseContext, item, Toast.LENGTH_SHORT).show()
            val toyAgeGroupChoice = parent.getItemAtPosition(position).toString()


            toyAgeGroup = toyAgeGroupChoice.split(" ")[0]


        }

        toyAgeGroupInputLayout.setEndIconOnClickListener {
            toyAgeGroupSpinner.showDropDown()
        }

        addImageBtn.setOnClickListener {

            pickImage.launch("image/*")

        }

        deleteToyBtn.setOnClickListener { deleteDataFromFirestore(toyID.toString()) }

        editToyBtn.setOnClickListener {
            disableAddToyBtn()

            edtToyId.error = null
            if (edtToyId.text.toString().isEmpty()) {
                edtToyIdLayout.error = getString(R.string.required)
                enableAddToyBtn()
            } else if (edtToyName.text.toString().isEmpty()) {
                edtToyIdLayout.error = null
                edtToyNameLayout.error = getString(R.string.required)
                enableAddToyBtn()

            } else if (edtToyRent.text.toString().isEmpty()) {
                edtToyNameLayout.error = null
                edtToyRentalLayout.error = getString(R.string.required)
                enableAddToyBtn()

            } else if (edtToyCategory.text.toString().isEmpty()) {
                edtToyRentalLayout.error = null
                edtToyCategoryLayout.error = getString(R.string.required)
                enableAddToyBtn()

            } else if (isAvailable.toString().isEmpty()) {
                edtToyCategoryLayout.error = null
                isAvailable = toy?.isAvailable == true
                enableAddToyBtn()

            } else if (toyAgeGroup.isEmpty()) {
                toyAgeGroup=toy?.toyAgeGroup.toString()
                toyAgeGroupInputLayout.error = getString(R.string.required)
                enableAddToyBtn()

            } else if (edtToyDesc.text.toString().isEmpty()) {
                toyAgeGroupInputLayout.error = null
                edtToyDescLayout.error = getString(R.string.required)
                enableAddToyBtn()

            } else if (selectedImage == null && isImageChanged) {
                edtToyDescLayout.error = null
                addImageBtn.error = getString(R.string.required)
                addImageBtn.text = getString(R.string.required)
                addImageBtn.setTextColor(resources.getColor(R.color.errorColor, theme))
                enableAddToyBtn()


            } else {
                addImageBtn.error = null
                addImageBtn.text = "Image Added"
                addImageBtn.setTextColor(resources.getColor(R.color.primaryText, theme))
                AlertDialog.Builder(this)
                    .setTitle("Edit Toy")
                    .setMessage("Update toy info? Old info will be lost")
                    .setPositiveButton(
                        "Yes"
                    ) { _, _ ->
                        Toast.makeText(baseContext, "Updating toy to system", Toast.LENGTH_SHORT)
                            .show()
                        disableAddToyBtn()
                        if(isImageChanged){
                            uploadImageToStorage(selectedImage!!)
                        }
                        else{
                            saveDataToFirestore(toy?.toyImage.toString())
                        }
                        finish()

                    }
                    .setNegativeButton("No", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()

            }


        }

    }


    @RequiresApi(Build.VERSION_CODES.P)
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImage = compressImage(it)

            }
        }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun compressImage(imageUri: Uri): ByteArray {
        val contentResolver: ContentResolver = contentResolver

        // Get bitmap from Uri using ImageDecoder (non-deprecated)
        val source = ImageDecoder.createSource(contentResolver, imageUri)
        val bitmap: Bitmap = ImageDecoder.decodeBitmap(source)

        // Compress bitmap to byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, byteArrayOutputStream)
        addImageBtn.text = "Image Added"

        isImageChanged=true
        return byteArrayOutputStream.toByteArray()
    }

    private fun uploadImageToStorage(byteArray: ByteArray) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        val uploadTask = imageRef.putBytes(byteArray)
        uploadTask.addOnSuccessListener {
            // Image uploaded successfully, get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Now you can store the URI along with other details in Firestore
                saveDataToFirestore(uri.toString())

            }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseCloudLOG++", "Error uploading image: $exception")
            // Handle error
        }
    }

    private fun saveDataToFirestore(imageUri: String) {


        val toy = Toy(
            edtToyId.text.toString().toInt(),
            edtToyName.text.toString(),
            edtToyRent.text.toString().toLong(),
            edtToyCategory.text.toString(),
            isAvailable, toyAgeGroup, edtToyDesc.text.toString(),
            imageUri
        )
        saveCategory(edtToyCategory.text.toString())


        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Toys").document(edtToyId.text.toString())
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {


                    docRef.set(toy)
                        .addOnSuccessListener {
                            // Data saved successfully
                            Toast.makeText(
                                baseContext,
                                "Toy info updated successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                            //TODO finish after adding toy
                            enableAddToyBtn()
//                            finish()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("TAG", "Error saving data to Firestore: $exception")
                            // Handle error
                        }
                }

            }


    }

    private fun deleteDataFromFirestore(toyId: String) {

        AlertDialog.Builder(this)
            .setTitle("Delete Toy")
            .setMessage("Are you sure?")
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
                FirebaseFirestore.getInstance()
                    .collection("Toys")
                    .document(toyId)
                    .delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Toy with Id: $toyId deleted",

                                Toast.LENGTH_SHORT
                            ).show()
                            val dataMap = mutableMapOf<String, Any>()
                            dataMap["total"] = ServerValue.increment(-1)
                            dataMap["Infants"]= ServerValue.increment(0)
                            dataMap["Toddlers"]= ServerValue.increment(0)
                            dataMap["Preschoolers"]= ServerValue.increment(0)
                            dataMap["School-Age"]= ServerValue.increment(0)
                            dataMap["Teens"]= ServerValue.increment(0)
                            dataMap[toy?.toyAgeGroup.toString()] = ServerValue.increment(-1)
                            FirebaseDatabase
                                .getInstance()
                                .getReference("ToyStats")
                                .updateChildren(dataMap)


                        }

                    }

                finish()

            }
            .setNegativeButton("No", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()



    }

    private fun saveCategory(category: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Categories").document(category)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    Log.d("Cat", "Category already exists")
                } else {
                    data class x(val x: String)
                    docRef.set(x("x"))

                }

            }
        }
    }

    private fun disableAddToyBtn() {
        editToyBtn.isEnabled = false
        editToyBtn.text = "Adding..."
        editToyBtn.setBackgroundColor(resources.getColor(R.color.disabled, theme))
    }

    private fun enableAddToyBtn() {
        editToyBtn.isEnabled = true

        editToyBtn.text = "Add toy"
        editToyBtn.setBackgroundColor(resources.getColor(R.color.primary, theme))
    }

    private fun setup(toyId: String?) {


         FirebaseFirestore.getInstance()
            .collection("Toys")
            .document(toyId.toString())
            .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    toy = document.toObject()
                    edtToyName.setText(toy?.toyName)
                    edtToyRent.setText(toy?.toyRent.toString())
                    edtToyCategory.setText(toy?.toyCategory.toString())
                    toyAvailInputLayout.helperText="Currently: ${toy?.isAvailable}"
                    toyAgeGroupInputLayout.helperText="Currently: ${toy?.toyAgeGroup}"
                    edtToyDesc.setText(toy?.toyDesc)
                }
                }
            }
        }
    }

















