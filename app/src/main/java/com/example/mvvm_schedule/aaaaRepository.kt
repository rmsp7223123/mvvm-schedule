package com.example.mvvm_schedule

import android.util.Log
import androidx.lifecycle.LiveData
import java.util.Date

class aaaaRepository(private val aaaaDao: aaaaDao) {

    fun getEvents(selectDate : String) : LiveData<List<aaaa>> {
        return aaaaDao.getEventsForDate(selectDate);
    }

    fun all() : LiveData<List<aaaa>> {
        val events = aaaaDao.all();
        Log.d("Repository", "Fetched ${events.value?.size ?: 0} events from database");
        return events;
    }

    suspend fun insert(aaaa: aaaa) {
        return aaaaDao.insert(aaaa);
    }
}