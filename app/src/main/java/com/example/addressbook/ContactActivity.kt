package com.example.addressbook

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.addressbook.databinding.ActivityContactBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding

    private var purpose: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        purpose = intent.getStringExtra("purpose")

        setTitle("${purpose} Note")

        if (purpose == "View") {
            val id = intent.getLongExtra("id", 0L)
            viewContact(id)
        }
        else if(purpose == "Add"){

        }
    }

    fun viewContact(id: Long) {
        binding.firstNameEditText.isEnabled = false
        binding.lastNameEditText.isEnabled = false
        binding.addressEditText.isEnabled = false
        binding.emailEditText.isEnabled = false
        binding.phoneNumberEditText.isEnabled = false
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
        builder.setMessage("Are you sure you want to delete this Note?")
        builder.setPositiveButton("Yes", listener)
        builder.setNegativeButton(android.R.string.cancel, listener)
        builder.show()


    }



    override fun onBackPressed() {
        if (purpose == "Add") {
            val title = binding.titleEditText.text.toString()
            if (title == "") {
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("Title Empty")
                builder.setMessage("This note must have a title to be saved")

                builder.show()
                return;
            }
            CoroutineScope(Dispatchers.IO).launch {

                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.noteDao()
                val title = binding.titleEditText.text.toString()

                val text = binding.noteEditText.text.toString()
                val now = Date()
                val databaseDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                databaseDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
                var dateString: String = databaseDateFormat.format(now)

                var note = Note(-1, title, text, dateString)

                dao.insertNote(note)
                Log.i(purpose, "added note")
            }
        } else if (purpose == "Update") {
            val fName = binding.firstNameEditText.text.toString()
            val lName = binding.firstNameEditText.text.toString()
            if (fName == "" || lName == "") {
                Toast.makeText(
                    applicationContext,
                    "Please enter a first and last name for this Contact",
                    Toast.LENGTH_LONG
                ).show()
                return;
            }
            CoroutineScope(Dispatchers.IO).launch {

                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.contactDao()

                val text = binding.descriptionEditText.text.toString()
                val phone = binding.phoneNumberEditText.text.toString()
                val email = binding.emailEditText.text.toString()

                val intent = intent
                val id = intent.getLongExtra("id", 0L)

                var note = Contact(id, fName, lName, text, )

                dao.updateNote(note)
                Log.i(purpose, "updated note")
            }
        }
        super.onBackPressed()
    }
}