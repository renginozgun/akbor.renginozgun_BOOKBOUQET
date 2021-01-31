package com.example.akbor_renginozgun_bookbouqet

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.akbor_renginozgun_bookbouqet.R
import com.example.akbor_renginozgun_bookbouqet.BookClass
import com.example.akbor_renginozgun_bookbouqet.db.DataManager
import kotlinx.android.synthetic.main.edit_dialog.view.*
import kotlinx.android.synthetic.main.book_cardview.view.page

class BookAdapter(val list: ArrayList<BookClass>, val context: Context): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    //Adapter member functions
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Get the book item on clicked position
        holder.bindViews(list[position])
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        //Set book cardview layout
        val view = LayoutInflater.from(context).inflate(R.layout.book_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        //Define properties
        val name = itemView.findViewById<TextView>(R.id.book_name)
        val author = itemView.findViewById<TextView>(R.id.author)
        val page = itemView.findViewById<TextView>(R.id.page)

        val delete = itemView.findViewById<ImageButton>(R.id.delete)
        val edit = itemView.findViewById<ImageButton>(R.id.edit)

        fun bindViews(book: BookClass) {

            //Assign book data values to layout properties
            name.text = book.bookName
            author.text = book.author
            page.text = book.page

            //If read property is true, change background color
            if(book.read==true){
                itemView.findViewById<CardView>(R.id.card_view_book).setCardBackgroundColor(Color.parseColor("#F08080"))
            }else{
                itemView.findViewById<CardView>(R.id.card_view_book).setCardBackgroundColor(Color.parseColor("#8F9B356D"))
            }
            //Set on click listeners fot edit and delete buttons so that later they can be used
            delete.setOnClickListener(this)
            edit.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            var bPosition = adapterPosition
            var book = list[bPosition]

            when(view!!.id) {
                delete.id -> {
                    //When delete button is clicked, set an alert.
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Are you sure you that want to delete ?")
                        .setCancelable(false)
                            //If yes is clicked, delete the item.
                        .setPositiveButton("Yes") { dialog,id ->
                            deleteBook(book.id!!.toInt())
                            list.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                        }
                        .setNegativeButton("No") { dialog,id ->
                            // If no is clicked, dismiss the dialog
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()

                }
                edit.id -> {
                    editBook(book)
                }
            }
        }

        // Delete the book

        fun deleteBook(id: Int) {
            val db = DataManager(context)
            db.removeBook(id)

        }
        //Edit the book
        fun editBook(book: BookClass) {

            //Define database handler and layout
            val dbHandler = DataManager(context)
            var myView = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null)

            myView.edit_title.setText(book.bookName)
            myView.edit_author.setText(book.author)
            myView.page.setText(book.page)

            val saveButton = myView.saveButton
            //Set, create and show dialog
            val dialogBuilder = AlertDialog.Builder(context).setView(myView)
            val dialog = dialogBuilder.create()
            dialog.show()

            saveButton.setOnClickListener {
                //Set the properties with item's data
                var name = myView.edit_title
                val author = myView.edit_author
                val page = myView.page
                var check= myView.check2
                // Get Updated values
                book.bookName = name.text.toString()
                book.author = author.text.toString()
                //If check is checked, assign true to read
                if(check.isChecked){ book.read=true}

                book.page = page.text.toString()
                //Update the book
                dbHandler.updateBook(book)
                notifyItemChanged(adapterPosition, book)

                //Close the dialog layout
                dialog.dismiss()


            }

        }

    }

}


