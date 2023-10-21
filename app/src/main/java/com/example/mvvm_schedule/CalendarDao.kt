package com.example.mvvm_schedule

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface CalendarDao {
    @Insert
    suspend fun insert(calendar: Calendar);

    @Query("SELECT * FROM calendar WHERE strftime('%Y-%m-%d', date) = :selectDate")
    fun getEventsForDate(selectDate: String): LiveData<List<Calendar>>
}