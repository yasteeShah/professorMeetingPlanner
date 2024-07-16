package com.example.professormeetingplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val roleEditText = findViewById<EditText>(R.id.role)

        val loginButton = findViewById<Button>(R.id.login)
        val signupButton = findViewById<Button>(R.id.signup)

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val role = roleEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid

                    val userData = mapOf(
                        "fName" to "First",
                        "lName" to "Last",
                        "role" to role,
                        "email" to email
                    )

                    userId?.let {
                        database.child("users").child(it).setValue(userData).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.child("users").child(auth.currentUser!!.uid).get().addOnSuccessListener {
                        val role = it.child("role").value.toString()
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("ROLE", role)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
