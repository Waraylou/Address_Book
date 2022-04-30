package com.example.addressbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.addressbook.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        val layoutManager = LinearLayoutManager(this)
        binding.contactView.setLayoutManager(layoutManager)

        val divider = DividerItemDecoration(
            applicationContext, layoutManager.orientation
        )
        binding.contactView.addItemDecoration(divider)

        adapter = MyAdapter()
        binding.contactView.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val dao = db.contactDao()
            val results = dao.getContactsByTitle()

            withContext(Dispatchers.Main) {
                data.clear()
                data.addAll(results)
                adapter.notifyDataSetChanged()
            }
        }

    }


    override fun onResume() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val dao = db.contactDao()
            val results = dao.getContactsByTitle()

            withContext(Dispatchers.Main) {
                data.clear()
                data.addAll(results)
                adapter.notifyDataSetChanged()
            }
        }
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_menu_item) {
            val intent = Intent(applicationContext, ContactActivity::class.java)
            intent.putExtra("purpose", "Add")

            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
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
            var title = data[position].firstName + " " + data[position].lastName


            holder.setText(title)
        }

        override fun getItemCount(): Int {
            return data.size
            return 0;
        }

    }
}