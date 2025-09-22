package com.zfh.teleprompter.db

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.zfh.teleprompter.db.database.AppDatabase
import com.zfh.teleprompter.db.entry.FloatText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DBHelper {

    private val scope = CoroutineScope(Dispatchers.IO)

    lateinit var db: AppDatabase

    fun init(context: Context) {
        db = Room.databaseBuilder(context, AppDatabase::class.java, "teleprompter_db").build()
    }

    fun insert(msg: String) {
        try {
            scope.launch {
                db.floatTextDao().insert(FloatText(msg, System.currentTimeMillis()))
            }
        } catch (e: Exception) {
            Log.e("DBHelper", "insert fail, msg:$e")
        }
    }

    fun getAllFloatText(): LiveData<List<FloatText>> {
        return db.floatTextDao().getAll()
    }

    fun delete(msg: String) {
        try {
            scope.launch {
                db.floatTextDao().deleteByText(msg)
            }
        } catch (e: Exception) {
            Log.e("DBHelper", "delete fail, msg:$e")
        }
    }

}