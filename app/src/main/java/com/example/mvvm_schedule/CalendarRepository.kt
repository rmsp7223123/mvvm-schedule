package com.example.mvvm_schedule

import androidx.lifecycle.LiveData

class CalendarRepository(private val calendarDao: CalendarDao) {
    fun getEvents(selectDate : Long) : LiveData<List<Calendar>> {
        return calendarDao.getEventsForDate(selectDate);
    }

    suspend fun insert(calendar : Calendar) {
        return calendarDao.insert(calendar);
    }
}