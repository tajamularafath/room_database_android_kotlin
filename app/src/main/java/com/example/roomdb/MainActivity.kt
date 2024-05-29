package com.example.roomdb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.roomdb.DataBase.AppDatabase
import com.example.roomdb.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var btnSave: Button

    //    private lateinit var btnDelete : Button
    private lateinit var myAdapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var dataList: List<User>
    private val users: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSave = findViewById(R.id.btnText)
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.main_rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        btnDelete = findViewById(R.id.btnDelete)
        myAdapter = MyAdapter(users)
        recyclerView.adapter = myAdapter


//        Delete Data
        myAdapter.setOnClickListener(object : MyAdapter.OnClickListener {
            override fun onClick(position: Int, model: User) {
                lifecycleScope.launch {
                    val userDao = db.userDao()
                    userDao.deleteById(model.id)
                    users.removeAt(position)
                    myAdapter.notifyItemRemoved(position)
                }
                Log.e("===>", "Deleted by id: ${model.id}")
            }
        })

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "myDataBase"
        ).build()

        // Call the suspend function within a coroutine
        /*        lifecycleScope.launch {
                    callon()
                    val userDao = db.userDao()
                   users.addAll(userDao.getAll())

                }*/


//        Adding Data
        btnSave.setOnClickListener {
            val firstName = findViewById<EditText>(R.id.firstInput).text.toString()
            val secondName = firstName
            if (firstName.isNotEmpty() && secondName.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    addData(firstName, secondName)
                }
                findViewById<EditText>(R.id.firstInput).setText("")
            } else {
                Toast.makeText(this@MainActivity, "Please enter Name", Toast.LENGTH_SHORT).show()
            }
        }

//         Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch(Dispatchers.IO) {
            getAllData()
        }
    }

    private suspend fun callon() {
        val userDao = db.userDao()
        val users: List<User> = userDao.getAll()
        Log.e("===>", users.toString())
    }


    private suspend fun addData(firstName: String, lastName: String) {
        val newUser = User(firstName = firstName, lastName = lastName)

        lifecycleScope.launch(Dispatchers.IO) {
            db.userDao().insertAll(newUser)
            users.add(newUser)

            runOnUiThread {
                Toast.makeText(this@MainActivity, "User Inserted", Toast.LENGTH_SHORT).show()
                myAdapter.notifyItemInserted(users.lastIndex)

            }
        }
    }

    private suspend fun getAllData() {
        val u = db.userDao().getAll()
        withContext(Dispatchers.Main) {
            users.addAll(u)
            myAdapter.notifyDataSetChanged()
        }
    }
}

//fun main() {
//    val map1 = mutableMapOf<Int,String>()
//    map1.put(1, "Hello World")
//    map1.put(2, "Hello World")
//    println(map1)
//
//    val hashmap1 = hashMapOf<Int,String>()
//    hashmap1.put(1,"John")
//    hashmap1.put(2,"John")
//    hashmap1.put(3,"Umar")
//    println(hashmap1)
//
//}