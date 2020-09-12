package com.example.musicApp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.musicApp.data.Album
import com.example.musicApp.room.AppData


class AlbumListViewModel(application: Application) : AndroidViewModel(application) {

    //  var aMutableUserList: LiveData<List<Album>>? = null

    var mAppData: AppData? = null

    init {
        mAppData = AppData.getInstance(application)
    }


    fun getAllAlbums(): LiveData<List<Album>>? {
        val aUserList = mAppData!!.albumDao().geAllAlbums()
        return aUserList

    }

    fun getAlbumWithQuery(filterQuery: SimpleSQLiteQuery): LiveData<List<Album>>? {
        val aUserList = mAppData!!.albumDao().getAlbumWithQuery(filterQuery)
        return aUserList
    }

}
