package com.example.mvvm_schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CalendarRepository;
    private val selectedDate = MutableLiveData<Date>();
    private val _eventsForSelectedDate = MutableLiveData<List<Calendar>>();
    val eventsForSelectedDate: LiveData<List<Calendar>> get() = _eventsForSelectedDate;

    init {
        val eventDao = CalendarDatabase.getDatabase(application).calendarDao();
        repository = CalendarRepository(eventDao);
    };

    fun setSelectedDate(dateString: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            val date = dateFormat.parse(dateString);
            selectedDate.value = date;
            loadEventsForSelectedDate(dateString);
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    fun insertEvent(calendar: Calendar) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(calendar);
        };
    };

    fun loadEventsForSelectedDate(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val events = repository.getEvents(date);
            withContext(Dispatchers.Main) {
                _eventsForSelectedDate.value = events.value ?: emptyList();
            };
        };
    };
}
