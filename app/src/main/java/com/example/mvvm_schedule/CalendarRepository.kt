package com.example.mvvm_schedule

import androidx.lifecycle.LiveData
import java.util.Date

class CalendarRepository(private val calendarDao: CalendarDao) {
    fun getEvents(selectDate : Date) : LiveData<List<Calendar>> {
        return calendarDao.getEventsForDate(selectDate);
    }

    suspend fun insert(calendar : Calendar) {
        return calendarDao.insert(calendar);
    }
}