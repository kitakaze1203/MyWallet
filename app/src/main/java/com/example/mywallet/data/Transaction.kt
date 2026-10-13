package com.example.mywallet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val amount: Long,

    val timestamp: Long
)