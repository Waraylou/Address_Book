package com.example.addressbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.addressbook.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var data = mutableListOf<Contact>()
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


    inner class MyViewHolder(val view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.findViewById<View>(R.id.item_constraintLayout)
                .setOnClickListener(this)
        }

        fun setText(text: String) {
            view.findViewById<TextView>(R.id.item_title_textView).setText(text)

        }

        override fun onClick(view: View?) {
            if (view != null) {
                val intent = Intent(view.context, ContactActivity::class.java)
                intent.putExtra("purpose","View")
                intent.putExtra("id", data[adapterPosition].contactId)
                startActivity(intent)
            }
        }

    }


    inner class MyAdapter() : RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            var title = data[position].name


            holder.setText(title)
        }

        override fun getItemCount(): Int {
            return data.size
            return 0;
        }

    }
}