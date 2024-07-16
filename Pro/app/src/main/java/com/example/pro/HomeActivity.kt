package com.example.professormeetingplanner

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        role = intent.getStringExtra("ROLE") ?: ""
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val homeTitle = findViewById<TextView>(R.id.home_title)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (role == "professor") {
            homeTitle.text = "Professor Home"
            // Fetch and display professor-specific data
        } else {
            homeTitle.text = "Student Home"
            // Fetch and display student-specific data
        }

        // Set up recycler view and other UI components
    }
}

