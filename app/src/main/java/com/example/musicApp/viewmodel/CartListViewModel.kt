package com.example.musicApp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.musicApp.data.Album
import com.example.musicApp.room.AppData


class CartListViewModel(application: Application) : AndroidViewModel(application) {

    var mAppData: AppData? = null

    init {
        mAppData = AppData.getInstance(application)
    }


    fun getCartList(ids: IntArray): LiveData<List<Album>>? {
        val aList = mAppData!!.albumDao().getCartList(ids)
        return aList

    }

}
