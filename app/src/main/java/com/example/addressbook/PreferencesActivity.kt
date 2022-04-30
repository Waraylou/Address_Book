package com.example.addressbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.addressbook.databinding.ActivityPreferencesBinding

class PreferencesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}