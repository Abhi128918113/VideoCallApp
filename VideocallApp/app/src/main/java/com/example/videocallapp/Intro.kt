package com.example.videocallapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔁 Check SharedPreferences - skip intro if already filled
        val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val savedName = sharedPref.getString("name", null)
        val savedPhone = sharedPref.getString("phone", null)
        if (!savedName.isNullOrEmpty() && !savedPhone.isNullOrEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // prevent coming back here
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner = findViewById<Spinner>(R.id.spinnerCountryCode)
        val etName = findViewById<EditText>(R.id.etName)
        val etPhone = findViewById<EditText>(R.id.etPhoneNumber)
        val tvError = findViewById<TextView>(R.id.tvErrorMessage)
        val nextButton = findViewById<Button>(R.id.btnNext)

        val countryCodes = listOf("+1", "+44", "+91", "+81", "+49", "+33", "+61", "+86", "+7", "+39")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryCodes).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.adapter = adapter

        nextButton.setOnClickListener {
            val name = etName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val countryCode = spinner.selectedItem.toString()

            if (name.isEmpty() || phone.isEmpty()) {
                tvError.text = "Please fill in both name and phone number"
                tvError.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                tvError.visibility = TextView.VISIBLE
            } else {
                // ✅ Save name, phone and country code to SharedPreferences
                with(sharedPref.edit()) {
                    putString("name", name)
                    putString("phone", phone)
                    putString("country_code", countryCode)
                    apply()
                }

                tvError.visibility = TextView.GONE
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
