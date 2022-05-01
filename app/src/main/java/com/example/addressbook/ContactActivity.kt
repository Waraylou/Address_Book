package com.example.addressbook

import android.R.color
import android.content.Context
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

    private var menuRef: Menu? = null

    private var darkMode = false

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
        } else if (purpose == "Add") {

        }
    }

    override fun onStart() {
        super.onStart()

        val preferences = getSharedPreferences(
            "app_preferences",
            Context.MODE_PRIVATE
        )
        darkMode = preferences.getBoolean("dark_mode", false)
        if (darkMode) {
            val color = Color.parseColor("#FFFFFF")
            val black = Color.parseColor("#000000")
            val grey = Color.parseColor("#6F6F6F")
            val underline = ColorStateList.valueOf(color);


            binding.myConstraintLayout.setBackgroundColor(black)
            binding.firstNameEditText.setTextColor(color)
            binding.firstNameEditText.backgroundTintList = underline
            binding.lastNameEditText.setTextColor(color)
            binding.lastNameEditText.backgroundTintList = underline
            binding.addressEditText.setTextColor(color)
            binding.addressEditText.backgroundTintList = underline
            binding.emailEditText.setTextColor(color)
            binding.emailEditText.backgroundTintList = underline
            binding.phoneNumberEditText.setTextColor(color)
            binding.phoneNumberEditText.backgroundTintList = underline
            binding.descriptionEditText.setTextColor(color)
            binding.descriptionEditText.setBackgroundColor(grey)

            binding.firstNameLabel.setTextColor(color)
            binding.lastNameLabel.setTextColor(color)
            binding.addressLabel.setTextColor(color)
            binding.emailLabel.setTextColor(color)
            binding.phoneNumberLabel.setTextColor(color)
            binding.descriptionLabel.setTextColor(color)

            binding.mapIcon.setImageResource(R.drawable.ic_dark_map)
            binding.mailIcon.setImageResource(R.drawable.ic_dark_mail)
            binding.textIcon.setImageResource(R.drawable.ic_dark_text)

            binding.divider.setBackgroundColor(grey)
            binding.divider5.setBackgroundColor(grey)
            binding.divider2.setBackgroundColor(grey)
            binding.divider3.setBackgroundColor(grey)
            binding.divider4.setBackgroundColor(grey)

            var blue = Color.parseColor("#21AAFF")
            if (darkMode) {
                blue = Color.parseColor("#009AD0")
            }

            binding.emailEditText.setTextColor(blue)
            binding.addressEditText.setTextColor(blue)
            binding.phoneNumberEditText.setTextColor(blue)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.contact_menu, menu)
        if (purpose == "Add") {
            menu?.findItem(R.id.edit_menu_item)?.isVisible = false
            menu?.findItem(R.id.delete_menu_item)?.isVisible = false
            menu?.findItem(R.id.cancel_menu_item)?.isVisible = true
            menu?.findItem(R.id.save_menu_item)?.isVisible = true

        }
        menuRef = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit_menu_item) {
            binding.addressEditText.setOnClickListener(null)
            binding.emailEditText.setOnClickListener(null)
            binding.phoneNumberEditText.setOnClickListener(null)

            binding.firstNameEditText.isEnabled = true
            binding.lastNameEditText.isEnabled = true
            binding.addressEditText.isFocusable = true
            binding.addressEditText.isFocusableInTouchMode = true
            binding.emailEditText.isFocusable = true
            binding.emailEditText.isFocusableInTouchMode = true
            binding.phoneNumberEditText.isFocusable = true
            binding.phoneNumberEditText.isFocusableInTouchMode = true
            binding.descriptionEditText.isEnabled = true

            purpose = "Update"

            var test = binding.phoneNumberEditText.isFocusable

            setTitle("Edit Contact")
            item.isVisible = false
            var thing = menuRef?.findItem(R.id.save_menu_item)
            thing?.isVisible = true


        } else if (item.itemId == R.id.delete_menu_item) {
            deleteContact()
        } else if (item.itemId == R.id.cancel_menu_item) {
            purpose = "Canceled"
            Log.i(purpose, "Contact creation canceled")
            onBackPressed()
        } else if (item.itemId == R.id.save_menu_item) {
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

                    var color = Color.parseColor("#21AAFF")
                    if (darkMode) {
                        color = Color.parseColor("#009AD0")
                    }

                    binding.emailEditText.setTextColor(color)
                    binding.emailEditText.setOnClickListener {
                        if (binding.emailEditText.text.toString() != "") {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.putExtra(
                                Intent.EXTRA_EMAIL,
                                binding.emailEditText.text.toString()
                            )
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            } else {
                                val builder = AlertDialog.Builder(binding.root.context)
                                builder.setTitle("No Email Application Detected")
                                builder.setMessage("Please install a Email application to utilize this feature.")
                                builder.show()
                            }
                        } else {
                            val builder = AlertDialog.Builder(binding.root.context)
                            builder.setTitle("No Email Address Detected")
                            builder.setMessage("Please add an Email address to utilize this feature.")
                            builder.show()
                        }
                    }

                    binding.phoneNumberEditText.setTextColor(color)
                    binding.phoneNumberEditText.setOnClickListener {
                        if (binding.phoneNumberEditText.text.toString() != "") {
                            val intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse(
                                "smsto:"
                                        + binding.phoneNumberEditText.text.toString()
                            )
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            } else {
                                val builder = AlertDialog.Builder(binding.root.context)
                                builder.setTitle("No Text Message Application Detected")
                                builder.setMessage("Please install a Text Message application to utilize this feature.")
                                builder.show()
                            }
                        } else {
                            val builder = AlertDialog.Builder(binding.root.context)
                            builder.setTitle("No Phone Number Detected")
                            builder.setMessage("Please add a Phone Number to utilize this feature.")
                            builder.show()
                        }

                    }

                    binding.addressEditText.setTextColor(color)
                    binding.addressEditText.setOnClickListener {
                        if (binding.phoneNumberEditText.text.toString() != "") {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(
                                "geo:0,0?q="
                                        + binding.addressEditText.text.toString()
                            )
                            startActivity(intent)
                        } else {
                            val builder = AlertDialog.Builder(binding.root.context)
                            builder.setTitle("No Address Detected")
                            builder.setMessage("Please add an Address to utilize this feature.")
                            builder.show()
                        }
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

    fun deleteContact() {
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