package com.example.mvvm_schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CalendarRepository;
    private val selectedDate = MutableLiveData<Long>();

    init {
        val eventDao = CalendarDatabase.getDatabase(application).calendarDao();
        repository = CalendarRepository(eventDao);
    };

    fun setSelectedDate(date: Long) {
        selectedDate.value = date;
    };

    fun insertEvent(calendar: Calendar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(calendar);
        };
    };
}