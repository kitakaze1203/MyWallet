package com.example.mywallet.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Label(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
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

    val labelId: Long?
)

data class TransactionWithLabel(
    @Embedded
    val transaction: Transaction,

    val labelName: String?
)

data class LabelTotal(
    @Embedded
    val label: Label,

    val amount: Long
)