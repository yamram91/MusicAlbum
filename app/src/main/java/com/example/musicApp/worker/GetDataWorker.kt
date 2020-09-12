package com.example.musicApp.worker

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.musicApp.api.ApiService
import com.example.musicApp.room.AppData
import com.example.musicApp.data.SearchResponse
import kotlinx.coroutines.coroutineScope
import retrofit2.Response


class GetDataWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    val mContext =context
    override suspend fun doWork(): Result = coroutineScope {
        try {


            if(isInternetOn(mContext)) {
                val response: Response<SearchResponse> =
                    ApiService.create().searchAlbum("all").execute()
                if (response == null) {
                    Log.e(TAG, "Error database " + response.errorBody().toString())
                }
                if (response.isSuccessful) {
                    if (response.body()!!.resultCount > 0) {
                        val aData = response.body()!!
                        val albumList = aData!!.results
                        val distinctAlbums = albumList.distinctBy { it.trackName }
                        val database = AppData.getInstance(applicationContext)
                        database.albumDao().insertAll(distinctAlbums)

                    }
                } else {
                    Log.e(TAG, "Error database " + response.errorBody().toString())
                }
            }
            else {
                Log.e(TAG, "No internet ")
            }
            Result.success()
        } catch (ex: Exception) {
            Log.e(TAG, "Error database", ex)
            Result.failure()
        }
    }
    fun isInternetOn(aContext: Context): Boolean {
        val connectivityManager = aContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    companion object {
        private const val TAG = "GetDataWorker"
    }
}
