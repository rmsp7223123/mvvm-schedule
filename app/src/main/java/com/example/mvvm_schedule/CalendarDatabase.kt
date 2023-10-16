package com.example.mvvm_schedule

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Calendar::class], version = 1)
abstract class CalendarDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao;
    companion object {
        private var INSTANCE: CalendarDatabase? = null;

        fun getDatabase(context: Context): CalendarDatabase {
            if (INSTANCE == null) {
                synchronized(CalendarDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, CalendarDatabase::class.java, "calendar_database").build();
                };
            };
            return INSTANCE!!;
        };
    };
}