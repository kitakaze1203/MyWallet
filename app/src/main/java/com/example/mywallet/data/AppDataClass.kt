package com.example.mywallet.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mywallet.setting.Cycle

enum class Cycle(val code:String){
    YEAR("年毎"),
    MONTH("ヶ月毎"),
    DAY("日毎")
}

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
data class Subscribe(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    var basePaymentDay: Long,
    var nextPaymentDay: Long,
    val name: String,
    val amount: Long,
    val cycle: Cycle,
    val value: Long,
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