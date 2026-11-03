package com.example.mywallet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Label(
    var labelName: String,
    var expenseDefault: Boolean = false,
    var grossDefault: Boolean = false,
    var noDelete: Boolean = false,
)

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val amount: Long,

    val timestamp: Long,

    val label: String?
)