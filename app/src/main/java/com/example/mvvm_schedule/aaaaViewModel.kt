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

    val readAlldata : LiveData<List<aaaa>>;
    val eventsForSelectedDate: LiveData<List<aaaa>> get() = _eventsForSelectedDate;

    init {
        val eventDao = aaaaDatabase.getDatabase(application).aaaaDao();
        repository = aaaaRepository(eventDao);
        readAlldata = repository.all();
    };

    fun setSelectedDate(dateString: String) {
        try {
            loadEventsForSelectedDate(dateString);
        } catch (e: Exception) {
            e.printStackTrace();
        }
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

    fun all() {
        viewModelScope.launch(Dispatchers.IO) {
            val events = repository.all();
            Log.d("ViewModel", "Fetched ${events.value?.size ?: 0} events from repository");
            withContext(Dispatchers.Main) {
                _eventsForSelectedDate.value = events.value ?: emptyList();
            };
        }
    }
}
