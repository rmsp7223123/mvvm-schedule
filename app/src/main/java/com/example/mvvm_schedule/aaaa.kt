package com.example.mvvm_schedule

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "aaaa")
data class aaaa (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: String,
    var description: String,
    var importance : Int
)