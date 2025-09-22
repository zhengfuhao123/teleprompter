package com.zfh.teleprompter.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zfh.teleprompter.db.entry.FloatText

@Dao
interface FloatTextDao {

    @Query("Select * FROM floattext ORDER BY create_time DESC")
    fun getAll(): LiveData<List<FloatText>>

    @Query("SELECT * FROM floattext WHERE text IN(:str)")
    suspend fun getByText(str: String): List<FloatText>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg text: FloatText)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(text: FloatText)

    @Query("DELETE FROM floattext WHERE text =:str")
    suspend fun deleteByText(str: String)

}