package com.example.mvvm_schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : CalendarRepository;
    private val selectedDate = MutableLiveData<String>();
    private val _eventsForSelectedDate = MutableLiveData<List<Calendar>>();
    val eventsForSelectedDate: LiveData<List<Calendar>> get() = _eventsForSelectedDate;

    init {
        val userDao = CalendarDatabase.getDatabase(application).calendarDao();
        repository = CalendarRepository(userDao);
    };
    fun setSelectedDate(date: Date?) {
        selectedDate.value = String.toString();
        if (date != null) {
            loadEventsForSelectedDate(date);
        };
    };

    fun insertEvent(calendar: Calendar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(calendar);
        };
    };

    fun loadEventsForSelectedDate(date: Date) {
        val selectedDateString = formatDateFromTimestamp(date.time)
        viewModelScope.launch(Dispatchers.IO) {
            val events = repository.getEvents(selectedDateString)
            _eventsForSelectedDate.postValue(events.value)
        }
    };

    fun formatDateFromTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(Date(timestamp));
    };
}