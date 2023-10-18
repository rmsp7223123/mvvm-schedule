package com.example.mvvm_schedule

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.mvvm_schedule.databinding.ActivityMainBinding
import com.example.mvvm_schedule.databinding.DialogAddScheduleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding;

    private lateinit var dialog : Dialog;

    private lateinit var dialogBinding :DialogAddScheduleBinding;

    private var selectedDate = "";

    private var importance = 0;

    private lateinit var date : Date;

    private lateinit var repository: CalendarRepository;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        binding.addScheduleBtn.setOnClickListener {
            showAddDialog();
        };

        binding.calendarView.setOnDateChangeListener {_, year, month, dayOfMonth ->
        val formattedMonth = if (month < 9) "0${month + 1}" else "${month + 1}";
        val formattedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth";
        selectedDate = "$year-$formattedMonth-$formattedDay";
    }

        val calendarDatabase = Room.databaseBuilder(
            applicationContext,
            CalendarDatabase::class.java, "calendar-database"
        ).build();

        repository = CalendarRepository(calendarDatabase.calendarDao());

        dialog = Dialog(this);
        dialogBinding = DialogAddScheduleBinding.inflate(LayoutInflater.from(this));

        val calendar = Calendar.getInstance();
        val dateFormat = SimpleDateFormat("yyyy-MM-dd");
        selectedDate = dateFormat.format(calendar.time);
        try {
            date = dateFormat.parse(selectedDate);
        } catch (e: Exception) {
            e.printStackTrace();
        };

    };

    private fun showAddDialog() {
        dialog.setContentView(dialogBinding.root);
        dialogBinding.dateText.text = selectedDate;
        dialogBinding.saveScheduleBtn.setOnClickListener { _ ->
            if (dialogBinding.content.text.toString().isEmpty()) {
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (dialogBinding.radioGroup.checkedRadioButtonId === -1) {
                Toast.makeText(this, "중요도를 체크해주세요.", Toast.LENGTH_SHORT).show()
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
                val calendarData = Calendar(0, date, dialogBinding.content.toString(), importance);
                repository.insert(calendarData);
            } else {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "유효하지 않은 날짜입니다.", Toast.LENGTH_SHORT).show();
                };
            };
        };
    };
}