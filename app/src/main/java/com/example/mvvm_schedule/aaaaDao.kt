package com.example.mvvm_schedule

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface aaaaDao {
    @Insert
    suspend fun insert(aaaa: aaaa);

    @Query("SELECT * FROM aaaa WHERE date = :selectDate")
    fun getEventsForDate(selectDate: String): LiveData<List<aaaa>>

    @Query("SELECT * FROM aaaa")
    fun all(): LiveData<List<aaaa>>;
}