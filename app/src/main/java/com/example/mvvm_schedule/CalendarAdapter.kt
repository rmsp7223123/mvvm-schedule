package com.example.mvvm_schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_schedule.databinding.ItemCalendarListBinding

class CalendarAdapter(private var calendarList : List<Calendar>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    inner class ViewHolder(var binding : ItemCalendarListBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCalendarListBinding.inflate(LayoutInflater.from(parent.context), parent, false));
    }

    override fun getItemCount(): Int {
        return calendarList.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvContent.text = calendarList[position].description;
    };

    fun setData(newList: List<Calendar>) {
        calendarList = newList;
        notifyDataSetChanged();
    }
}