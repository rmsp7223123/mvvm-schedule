package com.example.mvvm_schedule

import android.app.Application
import android.util.Log
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

class aaaaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: aaaaRepository;
    private val _eventsForSelectedDate = MutableLiveData<List<aaaa>>();
    private val _allEvents = MutableLiveData<List<aaaa>>();

    val readAlldata : LiveData<List<aaaa>>;
    val eventsForSelectedDate: LiveData<List<aaaa>> get() = _eventsForSelectedDate;

    init {
        val eventDao = aaaaDatabase.getDatabase(application).aaaaDao();
        repository = aaaaRepository(eventDao);
        readAlldata = repository.all();
        // loadAllEvents();
    };

    fun setSelectedDate(dateString: String) {
        _eventsForSelectedDate.value = _allEvents.value?.filter { it.date == dateString } ?: emptyList();
    }

    fun insertEvent(aaaa: aaaa) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(aaaa);
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

    private fun loadAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val allEvents = repository.all();
            withContext(Dispatchers.Main) {
                _allEvents.value = allEvents.value ?: emptyList();
            };
        };
    };
}
