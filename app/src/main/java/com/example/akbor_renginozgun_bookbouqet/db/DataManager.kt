package com.example.akbor_renginozgun_bookbouqet.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.util.Log
import android.widget.Toast
import com.example.akbor_renginozgun_bookbouqet.BookClass

import java.util.ArrayList
import java.util.function.BinaryOperator


var DB_NAME= "book_database"
var DB_VERSION= 1
var TABLE_BOOKLIST= "booklist_table"
var _ID= "book_id"
var NAME= "book_name"
var AUTHOR= "book_author"
var PAGE= "book_page"
var READ= "book_read"

class DataManager (context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

//Create booklist table here
    override fun onCreate(db: SQLiteDatabase?) {

        val createQuery = "CREATE TABLE " + TABLE_BOOKLIST + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                AUTHOR + " TEXT," +
                PAGE + " TEXT," +
                READ + " INTEGER)" // Keep boolean value as 1 or 0 integer
        db?.execSQL(createQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKLIST)
        // create table again
        onCreate(db)
    }


//Add new book data to database
    fun createBook(book: BookClass) {
        val sqlDB: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        values.put(NAME, book.bookName)
        values.put(AUTHOR, book.author)
        values.put(PAGE, book.page)

        if(book.read){
            values.put(READ, 1)
        }else{values.put(READ, 0)}


        sqlDB.insert(TABLE_BOOKLIST, null, values)

        Log.d("DATA INSERTED", "success")
        sqlDB.close()
    }

// Fetch all the books from database so that it can be displayed when function called
    fun searchAll(): ArrayList<BookClass> {
        val sqlDB: SQLiteDatabase = writableDatabase
        val bookList: ArrayList<BookClass> = ArrayList()


        val selectAll = "SELECT * FROM " + TABLE_BOOKLIST
        val cursor: Cursor = sqlDB.rawQuery(selectAll, null)


        if(cursor.moveToFirst()) {
            do {
                var book = BookClass()
                book.id=cursor.getInt(cursor.getColumnIndex(_ID))
                book.bookName = cursor.getString(cursor.getColumnIndex(NAME))
                book.author = cursor.getString(cursor.getColumnIndex(AUTHOR))
                book.page = cursor.getString(cursor.getColumnIndex(PAGE))
                //Burada rengi de alabilirim
                bookList.add(book)

            } while (cursor.moveToNext())
        }
        return bookList
    }
// Get book as parameter and update it on database
    fun updateBook(book: BookClass) : Int {
        val sqlDB: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        values.put(NAME, book.bookName)
        values.put(AUTHOR, book.author)
        values.put(PAGE, book.page)
        if(book.read){
            values.put(READ, 1)
        }else{values.put(READ, 0)}


        //update book values based on book.id value
        return sqlDB.update(TABLE_BOOKLIST, values, _ID + "=?", arrayOf(book.id.toString()))
    }
// remove selected book based on id value
    fun removeBook(id: Int) {
        val sqlDB: SQLiteDatabase = writableDatabase
        sqlDB.delete(TABLE_BOOKLIST, _ID + "=?", arrayOf(id.toString()))
        sqlDB.close()
    }

    fun getChoresCount(): Int {
        val db: SQLiteDatabase = writableDatabase
        val countQuery = "SELECT * FROM " + TABLE_BOOKLIST
        val cursor: Cursor = db.rawQuery(countQuery, null)

        return cursor.count
    }

}