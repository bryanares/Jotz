package com.brian.jotz.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jotz")
data class Jotz (
        @PrimaryKey(autoGenerate = true)
        val id : Int = 0,

        @ColumnInfo(name = "title")
        val jotzTitle : String,

        @ColumnInfo(name = "body")
        val jotzBody : String
        )