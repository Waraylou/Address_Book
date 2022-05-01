package com.example.addressbook

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.RadioGroup
import com.example.addressbook.databinding.ActivityPreferencesBinding

class PreferencesActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private lateinit var binding: ActivityPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.darkModeCheckBox.setOnCheckedChangeListener(this)
    }

    override fun onStart() {
        super.onStart()

        val preferences = getSharedPreferences(
            "app_preferences",
            Context.MODE_PRIVATE
        )

        val darkMode = preferences.getBoolean("dark_mode",false)
        binding.darkModeCheckBox.isChecked = darkMode

        if (darkMode){
            val white = Color.parseColor("#FFFFFF")
            val grey = Color.parseColor("#6F6F6F")
            binding.myConstraintLayout.setBackgroundColor(grey)
            binding.darkModeCheckBox.setTextColor(white)
            binding.darkModeCheckBox.setBackgroundColor(white)
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
        val preferences = getSharedPreferences(
            "app_preferences",
            Context.MODE_PRIVATE
        )
        with(preferences.edit()){
            putBoolean("dark_mode", checked)
            apply()
        }
    }
}