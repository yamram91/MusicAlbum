package com.example.musicApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicApp.adapter.AlbumlistAdapter
import com.example.musicApp.data.Album
import com.example.musicApp.data.TRACKID
import com.example.musicApp.room.AppData
import com.example.musicApp.viewmodel.AlbumListViewModel
import com.example.musicApp.viewmodel.CartListViewModel

class CartActivity : AppCompatActivity() {

    var context: AppCompatActivity? = null
    lateinit var viewModel: CartListViewModel
    var recyclerView: RecyclerView? = null
    var recyclerViewAdapter: AlbumlistAdapter? = null
    var mAppData: AppData? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        context= this
        var ids = intent.extras!!.getIntArray(TRACKID) as IntArray
        recyclerView = findViewById(R.id.cartList)

        mAppData = AppData.getInstance(this)

        viewModel =  ViewModelProvider(this).get(CartListViewModel::class.java)

        viewModel!!.getCartList(ids)!!.observe(context!!, albumListObserver)

    }

    var albumListObserver: Observer<List<Album>> =
        object : Observer<List<Album>> {
            override fun onChanged(albumList: List<Album>) {

                recyclerViewAdapter = context?.let { AlbumlistAdapter(it,albumList,2) }
                recyclerView!!.layoutManager = LinearLayoutManager(context)
                recyclerView!!.adapter = recyclerViewAdapter
                if(albumList.isNullOrEmpty()){
                    Toast.makeText(context,"No Data Found!!", Toast.LENGTH_LONG).show()
                }
            }
        }
}