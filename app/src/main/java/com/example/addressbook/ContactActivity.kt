package com.example.addressbook

import android.R.color
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.addressbook.databinding.ActivityContactBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding

    private var purpose: String? = ""

    private var saveItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        purpose = intent.getStringExtra("purpose")

        setTitle("${purpose} contact")

        binding.phoneNumberEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        if (purpose == "View") {
            val id = intent.getLongExtra("id", 0L)
            viewContact(id)
        }
        else if(purpose == "Add"){

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.contact_menu, menu)
        if(purpose == "Add"){
            menu?.findItem(R.id.edit_menu_item)?.isVisible = false
            menu?.findItem(R.id.delete_menu_item)?.isVisible = false
            menu?.findItem(R.id.cancel_menu_item)?.isVisible = true
            menu?.findItem(R.id.save_menu_item)?.isVisible = true

            saveItem = menu?.findItem(R.id.save_menu_item)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit_menu_item) {
            binding.firstNameEditText.isEnabled = true
            binding.lastNameEditText.isEnabled = true
            binding.addressEditText.isFocusable = true
            binding.emailEditText.isFocusable = true
            binding.phoneNumberEditText.isFocusable = true
            binding.descriptionEditText.isEnabled = true

            purpose = "Update"

            setTitle("Edit Contact")
            // setting a note to edit mode removes the option from the menu as you are already doing that
            item.isVisible = false
            saveItem?.isVisible = true

            binding.addressEditText.setOnClickListener(null)
            binding.phoneNumberEditText.setOnClickListener(null)
            binding.emailEditText.setOnClickListener(null)


        } else if (item.itemId == R.id.delete_menu_item) {
            deleteContact()
        } else if(item.itemId == R.id.cancel_menu_item){
            purpose = "Canceled"
            Log.i(purpose, "Contact creation canceled")
            onBackPressed()
        } else if(item.itemId == R.id.save_menu_item){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun viewContact(id: Long) {
        binding.firstNameEditText.isEnabled = false
        binding.lastNameEditText.isEnabled = false
        binding.addressEditText.isFocusable = false
        binding.emailEditText.isFocusable = false
        binding.phoneNumberEditText.isFocusable = false
        binding.descriptionEditText.isEnabled = false
        if (id != 0L) {
            CoroutineScope(Dispatchers.IO).launch {

                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.contactDao()

                val contact = dao.getContact(id)
                withContext(Dispatchers.Main) {
                    binding.firstNameEditText.setText(contact.firstName)
                    binding.lastNameEditText.setText(contact.lastName)
                    binding.addressEditText.setText(contact.address)
                    binding.emailEditText.setText(contact.email)
                    binding.phoneNumberEditText.setText(contact.phone_number)
                    binding.descriptionEditText.setText(contact.description)

                    val color = Color.parseColor("#21AAFF")

                    binding.emailEditText.setTextColor(color)
                    binding.emailEditText.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.putExtra(Intent.EXTRA_EMAIL, binding.emailEditText.text.toString())
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }else{
                            val builder = AlertDialog.Builder(binding.root.context)
                            builder.setTitle("No Email Application Detected")
                            builder.setMessage("Please install a Email application to utilize this feature.")
                            builder.show()
                        }
                    }

                    binding.phoneNumberEditText.setTextColor(color)
                    binding.phoneNumberEditText.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SENDTO)
                        intent.data = Uri.parse("smsto:"
                                + binding.phoneNumberEditText.text.toString())
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }else{
                            val builder = AlertDialog.Builder(binding.root.context)
                            builder.setTitle("No Text Message Application Detected")
                            builder.setMessage("Please install a Text Message application to utilize this feature.")
                            builder.show()
                        }

                    }

                    binding.addressEditText.setTextColor(color)
                    binding.addressEditText.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("geo:0,0?q="
                                + binding.addressEditText.text.toString())
                        startActivity(intent)
                    }
                }
            }
        } else {
            binding.firstNameEditText.setText("ERROR")
            binding.lastNameEditText.setText("ERROR")
            binding.addressEditText.setText("ERROR")
            binding.emailEditText.setText("ERROR")
            binding.phoneNumberEditText.setText("ERROR")
            binding.descriptionEditText.setText("ERROR")

        }
    }

    fun deleteContact(){
        val listener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    purpose = "Delete"
                    CoroutineScope(Dispatchers.IO).launch {

                        val db = AppDatabase.getDatabase(applicationContext)
                        val dao = db.contactDao()
                        val intent = intent
                        val id = intent.getLongExtra("id", 0L)

                        dao.deleteContact(id)

                        withContext(Dispatchers.Main) {
                            onBackPressed()
                        }
                    }
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    return;
                }
            }
        }


        val builder = AlertDialog.Builder(binding.root.context)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to delete this contact?")
        builder.setPositiveButton("Yes", listener)
        builder.setNegativeButton(android.R.string.cancel, listener)
        builder.show()


    }



    override fun onBackPressed() {
        if (purpose == "Add") {
            val fName = binding.firstNameEditText.text.toString()
            val lName = binding.lastNameEditText.text.toString()
            if (fName == "" || lName == "") {
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("First or Last Name Empty")
                builder.setMessage("This Contact must have a first and last name to be saved")

                builder.show()
                return;
            }
            CoroutineScope(Dispatchers.IO).launch {

                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.contactDao()

                val description = binding.descriptionEditText.text.toString()
                val phone = binding.phoneNumberEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val address = binding.addressEditText.text.toString()

                val intent = intent
                val id = intent.getLongExtra("id", 0L)

                var contact = Contact(id, fName, lName, phone, email, address, description)

                dao.insertContact(contact)
                Log.i(purpose, "added contact")
            }
        } else if (purpose == "Update") {
            val fName = binding.firstNameEditText.text.toString()
            val lName = binding.lastNameEditText.text.toString()
            if (fName == "" || lName == "") {
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("First or Last Name Empty")
                builder.setMessage("This Contact must have a first and last name to be saved")

                builder.show()
                return;
            }
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.contactDao()

                val description = binding.descriptionEditText.text.toString()
                val phone = binding.phoneNumberEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val address = binding.addressEditText.text.toString()

                val intent = intent
                val id = intent.getLongExtra("id", 0L)

                var contact = Contact(id, fName, lName, phone, email, address, description)

                dao.updateContact(contact)
                Log.i(purpose, "updated contact")
            }
        }
        super.onBackPressed()
    }
}