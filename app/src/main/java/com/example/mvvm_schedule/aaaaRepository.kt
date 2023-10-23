package com.example.mvvm_schedule

import androidx.lifecycle.LiveData
import java.util.Date

class aaaaRepository(private val aaaaDao: aaaaDao) {

    fun getEvents(selectDate : String) : LiveData<List<aaaa>> {
        return aaaaDao.getEventsForDate(selectDate);
    }

    suspend fun insert(aaaa: aaaa) {
        return aaaaDao.insert(aaaa);
    }
}