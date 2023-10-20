package com.example.mvvm_schedule

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.mvvm_schedule.databinding.ActivityMainBinding
import com.example.mvvm_schedule.databinding.DialogAddScheduleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    private lateinit var dialog: Dialog;

    private lateinit var dialogBinding: DialogAddScheduleBinding;

    private var selectedDate = "";

    private var importance = 0;

    private lateinit var date: Date;

    private lateinit var repository: CalendarRepository;

    private lateinit var adapter: CalendarAdapter;

    private lateinit var viewModel: CalendarViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        binding.addScheduleBtn.setOnClickListener {
            showAddDialog();
        };

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val formattedMonth = if (month < 9) "0${month + 1}" else "${month + 1}";
            val formattedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth";
            selectedDate = "$year-$formattedMonth-$formattedDay";
            viewModel.setSelectedDate(selectedDate);
            readData();
        };

        val calendarDatabase = Room.databaseBuilder(
            applicationContext,
            CalendarDatabase::class.java, "calendar-database"
        ).build();

        repository = CalendarRepository(calendarDatabase.calendarDao());

        viewModel = ViewModelProvider(this)[CalendarViewModel::class.java];

        dialog = Dialog(this);
        dialogBinding = DialogAddScheduleBinding.inflate(LayoutInflater.from(this));

        val calendar = Calendar.getInstance();
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(calendar.time);
        try {
            date = dateFormat.parse(selectedDate);
            readData();
        } catch (e: Exception) {
            e.printStackTrace();
        };

    };

    private fun showAddDialog() {
        dialog.setContentView(dialogBinding.root);
        dialogBinding.dateText.text = selectedDate;
        dialogBinding.saveScheduleBtn.setOnClickListener { _ ->
            if (dialogBinding.content.text.toString().isEmpty()) {
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (dialogBinding.radioGroup.checkedRadioButtonId === -1) {
                Toast.makeText(this, "중요도를 체크해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                for (i in 0 until dialogBinding.radioGroup.childCount) {
                    val btn = dialogBinding.radioGroup.getChildAt(i) as RadioButton
                    if (dialogBinding.lessBtn.isChecked) {
                        importance = 1;
                        addDialog();
                        break;
                    } else if (dialogBinding.middleBtn.isChecked) {
                        importance = 2;
                        addDialog();
                        break;
                    } else {
                        importance = 3;
                        addDialog();
                        break;
                    };
                };
                dialog.dismiss();
            };
        };
        dialogBinding.cancelDialogBtn.setOnClickListener { _ -> dialog.dismiss(); };
        dialog.show();
    };

    private fun addDialog() {
        CoroutineScope(Dispatchers.IO).launch {
            if (date != null) {
                val calendarData =
                    Calendar(0, date, dialogBinding.content.text.toString(), importance);
                viewModel.insertEvent(calendarData);
            } else {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "유효하지 않은 날짜입니다.", Toast.LENGTH_SHORT).show();
                };
            };
        };
    };

    private fun readData() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            val selectedDate = dateFormat.format(date);
            viewModel.setSelectedDate(selectedDate);
            viewModel.eventsForSelectedDate.observe(this) { events ->
                Log.d("EventSize", "Events size: ${events?.size ?: 0}");
                if (events != null && events.isNotEmpty()) {
                    adapter = CalendarAdapter(events);
                    binding.recvSchedule.adapter = adapter;
                    binding.recvSchedule.layoutManager = LinearLayoutManager(this);
                } else {
                    binding.emptyText.text = "ddddd";
                }
            };
        } catch (e: Exception) {
            e.printStackTrace();
        };
    };
}