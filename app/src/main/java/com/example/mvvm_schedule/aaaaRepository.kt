package com.example.mvvm_schedule

import androidx.lifecycle.LiveData
import java.util.Date

class aaaaRepository(private val aaaaDao: aaaaDao) {

    fun getEvents(selectDate : String) : LiveData<List<aaaa>> {
        return aaaaDao.getEventsForDate(selectDate);
    }

    fun all() : LiveData<List<aaaa>> {
        return aaaaDao.all();
    }

    suspend fun insert(aaaa: aaaa) {
        return aaaaDao.insert(aaaa);
    }
}