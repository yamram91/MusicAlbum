package com.example.musicApp.room
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.musicApp.data.Album


@Dao
interface AlbumDao {
    @Query("SELECT * FROM Album  order by DateTime(releaseDate) ")
    fun geAllAlbums(): LiveData<List<Album>>


    @Query("SELECT * FROM Album where trackId IN (:IDs)")
    fun getCartList(IDs:IntArray): LiveData<List<Album>>

    @RawQuery(observedEntities = [Album:: class])
    fun getAlbumWithQuery(sortQuery: SupportSQLiteQuery):LiveData<List<Album>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Album>)

}



