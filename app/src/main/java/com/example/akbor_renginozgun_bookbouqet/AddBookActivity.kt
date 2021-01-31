package com.example.akbor_renginozgun_bookbouqet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.akbor_renginozgun_bookbouqet.db.DataManager

import kotlinx.android.synthetic.main.book_cardview.view.*
import kotlinx.android.synthetic.main.edit_dialog.view.*
import java.util.zip.Inflater

class AddBookActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set the layout
        setContentView(R.layout.activity_add_book)

        //Create book class instance
        var book=BookClass()

        //Set button property and when clicked, get the entered values to book
        val button = findViewById<Button>(R.id.addButton)
        button.setOnClickListener {
            val dm = DataManager(this)
            //Get the entered values
            book.bookName= findViewById<EditText>(R.id.edit_title).text.toString()
            book.author=findViewById<EditText>(R.id.edit_author).text.toString()
            book.page=findViewById<EditText>(R.id.page).text.toString()

            book.read = false
            //Send book instance to function
            dm.createBook(book)
            //Display success message
            Toast.makeText(this, "Successfully added!", Toast.LENGTH_LONG).show()
            // Go back to main activity
            startActivity(Intent(this, MainActivity::class.java))
        }


    }

}