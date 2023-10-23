package com.example.mvvm_schedule

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [aaaa::class], version = 2)
@TypeConverters(Converters::class)
abstract class aaaaDatabase : RoomDatabase() {
    abstract fun aaaaDao(): aaaaDao;

    companion object {
        private var INSTANCE: aaaaDatabase? = null;

        fun getDatabase(context: Context): aaaaDatabase {
            if (INSTANCE == null) {
                synchronized(aaaaDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        aaaaDatabase::class.java,
                        "aaaa_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db);
                            }

                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db);
                                Log.d("RoomQuery", "Database opened");
                                val cursor = db.query("SELECT * FROM aaaa");
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