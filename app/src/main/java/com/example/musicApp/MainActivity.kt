package com.example.musicApp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.musicApp.adapter.AlbumlistAdapter
import com.example.musicApp.data.Album
import com.example.musicApp.data.DATE_FORMAT
import com.example.musicApp.data.TRACKID
import com.example.musicApp.room.AppData
import com.example.musicApp.viewmodel.AlbumListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), LifecycleOwner {
    var context: AppCompatActivity? = null
    lateinit var viewModel: AlbumListViewModel
    var recyclerView: RecyclerView? = null
    var recyclerViewAdapter: AlbumlistAdapter? = null
    var mAppData: AppData? = null
    var sortByQuery=""
    var searchQuery=""
    var query =""
    lateinit var albumsList: List<Album>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        recyclerView = findViewById(R.id.albumList)

        mAppData = AppData.getInstance(this)

        viewModel =  ViewModelProvider(this).get(AlbumListViewModel::class.java)

        viewModel!!.getAllAlbums()!!.observe(context!!, albumListObserver)

        radioGroup_sortBy.setOnCheckedChangeListener(
            { group, checkedId ->
                when(checkedId){
                R.id.rb_cName->{
                    sortByQuery =" ORDER BY CollectionName "
                } R.id.rb_tName->{
                    sortByQuery =" ORDER BY TrackName "
                } R.id.rb_aName->{
                    sortByQuery =" ORDER BY artistName "
                } R.id.rb_cPrice->{
                    sortByQuery =" ORDER BY CAST( CollectionPrice AS DECIMAL(8,3)) DESC "
                }
            }
             query =   " SELECT * FROM Album "+searchQuery +sortByQuery
                Log.d(" query: "," "+query)
                viewModel!!.getAlbumWithQuery(SimpleSQLiteQuery(query))!!.observe(context!!, albumListObserver)
            })
        img_search.setOnClickListener { view ->
            if(group_inrField.visibility==View.GONE)
                group_inrField.visibility=View.VISIBLE
            else
                group_inrField.visibility=View.GONE
        }
        textView_rDate.setOnClickListener {
            val _Calendar = Calendar.getInstance(TimeZone.getDefault())
            val _DatePickerDialog = DatePickerDialog(
                context as MainActivity,

                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var mon = monthOfYear+1
                    var month= mon.toString()
                    if(mon<10)
                        month="0"+mon
                    var day = dayOfMonth.toString()
                    if(dayOfMonth<10)
                        day="0"+dayOfMonth

                    _Calendar.set(Calendar.YEAR, year)
                    _Calendar.set(Calendar.MONTH, monthOfYear)
                    _Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    textView_rDate.setText(""+year+"-"+month+"-"+day)
                },
                _Calendar.get(Calendar.YEAR), _Calendar.get(Calendar.MONTH), _Calendar.get(Calendar.DAY_OF_MONTH)
            )

            _DatePickerDialog.setCancelable(true)
            _DatePickerDialog.show()
        }
        btn_all.setOnClickListener({
            view ->
            group_inrField.visibility=View.GONE
            editText_aName.setText("")
            editText_tName.setText("")
            editText_cName.setText("")
            editText_cPrice.setText("")
            textView_rDate.setText("")
            searchQuery=""
            rb_aName.isChecked= false
            rb_tName.isChecked= false
            rb_cName.isChecked= false
            rb_cPrice.isChecked= false
            viewModel!!.getAllAlbums()!!.observe(context!!, albumListObserver)
        })
        btn_search.setOnClickListener({
            view->
            if(editText_aName.text.isNullOrBlank()) {
                editText_aName.error = getString(R.string.error_artistname)
            }else  if(editText_tName.text.isNullOrBlank())
                editText_tName.error=getString(R.string.erroe_trackname)
            else{
                    group_inrField.visibility=View.GONE
                var sQBuilder= StringBuilder()
                if(editText_aName.text.toString().isNotEmpty())
                    sQBuilder.append(" ArtistName LIKE '%${editText_aName.text}%' AND ")
                if(editText_tName.text.toString().isNotEmpty())
                    sQBuilder.append(" TrackName LIKE '%${editText_tName.text}%' AND ")
                if(editText_cName.text.toString().isNotEmpty())
                    sQBuilder.append( " CollectionName LIKE '%${editText_cName.text}%' AND ")
                if(editText_cPrice.text.toString().isNotEmpty())
                    sQBuilder.append(" CollectionPrice =${editText_cPrice.text} AND ")
                if(textView_rDate.text.toString().isNotEmpty())
                    sQBuilder.append( "Date(ReleaseDate) = DATE('${textView_rDate.text}') AND ")
                if(sQBuilder.toString().isNotEmpty() )
                 searchQuery= " WHERE "+removeLastChar(sQBuilder.toString(),4)
                query =   " SELECT * FROM Album "+searchQuery +sortByQuery
                Log.d(" query: "," "+query)
                viewModel!!.getAlbumWithQuery(SimpleSQLiteQuery(query))!!.observe(context!!, albumListObserver)
            }
        })

        btn_movetocart.setOnClickListener({
                view ->
            var tIDS = IntArray(albumsList.size)
            albumsList.filter { it.isChecked==1 }.forEachIndexed { index,al->
                tIDS[index]= al.trackId!!
            }
            if(tIDS.size>0)
            {
                moveToCartActivity(tIDS)

            }
        })


    }

    private fun moveToCartActivity(tIDS: IntArray) {
        val intent = Intent(this, CartActivity::class.java)
        intent.putExtra(TRACKID,tIDS)
        startActivity(intent)
    }

    var albumListObserver: Observer<List<Album>> =
        object : Observer<List<Album>> {
            override fun onChanged(albumList: List<Album>) {
                 albumsList = albumList

                recyclerViewAdapter = context?.let { AlbumlistAdapter(it,albumList,1) }
                recyclerView!!.layoutManager = LinearLayoutManager(context)
                recyclerView!!.adapter = recyclerViewAdapter
                if(albumList.isNullOrEmpty()){
                 Toast.makeText(context,"No Data Found!!",Toast.LENGTH_LONG).show()
                }
            }
        }

    fun removeLastChar(str: String,len:Int): String {
        return if (str != "") {
            str.substring(0, str.length - len)
        } else {
            ""
        }
    }
}


