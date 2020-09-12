package com.example.musicApp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.musicApp.data.Album
import com.example.musicApp.data.DATABASE_NAME
import com.example.musicApp.worker.GetDataWorker


@Database(entities = [Album::class], version = 1, exportSchema = false)
abstract class AppData : RoomDatabase() {
    abstract fun albumDao(): AlbumDao

    companion object {
        @Volatile private var instance: AppData? = null

        fun getInstance(context: Context): AppData {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppData {
            val request = OneTimeWorkRequestBuilder<GetDataWorker>().build()
            WorkManager.getInstance().enqueue(request)

            return Room.databaseBuilder(context, AppData::class.java, DATABASE_NAME).addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    }
                )
                .build()
        }
    }
}
