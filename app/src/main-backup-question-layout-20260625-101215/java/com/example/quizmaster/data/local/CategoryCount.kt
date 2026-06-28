package com.example.quizmaster.data.local

import androidx.room.ColumnInfo

data class CategoryCount(
    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "count")
    val count: Int
)