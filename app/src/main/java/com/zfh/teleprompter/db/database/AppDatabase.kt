package com.zfh.teleprompter.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zfh.teleprompter.db.dao.FloatTextDao
import com.zfh.teleprompter.db.entry.FloatText

@Database(entities = [FloatText::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun floatTextDao(): FloatTextDao

}