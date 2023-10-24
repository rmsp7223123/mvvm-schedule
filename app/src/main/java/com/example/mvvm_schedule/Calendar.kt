package com.example.mvvm_schedule

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "calendar")
data class Calendar (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: String,
    var description: String,
    var importance : Int
)