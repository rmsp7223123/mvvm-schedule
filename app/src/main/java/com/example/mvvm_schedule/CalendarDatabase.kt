package com.example.mvvm_schedule

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Calendar::class], version = 2)
@TypeConverters(Converters::class)
abstract class CalendarDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao;

    companion object {
        private var INSTANCE: CalendarDatabase? = null;

        fun getDatabase(context: Context): CalendarDatabase {
            if (INSTANCE == null) {
                synchronized(CalendarDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CalendarDatabase::class.java,
                        "calendar_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db);
                            }

                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db);
                                Log.d("RoomQuery", "Database opened");
                                val cursor = db.query("SELECT * FROM calendar");
                                Log.d("RoomQuery", "SELECT query executed");
                                cursor.close();
                            }
                        })
                        .build();
                };
            };
            return INSTANCE!!;
        };
    };
}