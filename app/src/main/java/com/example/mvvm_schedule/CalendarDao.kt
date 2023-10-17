package com.example.mvvm_schedule

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalendarDao {
    @Insert
    suspend fun insert(calendar: Calendar);

    @Query("select * from calendar where date = :selectDate")
    fun getEventsForDate(selectDate : Long): LiveData<List<Calendar>>;
}