package com.example.mywallet.module

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.absoluteValue

fun formatAmount(amount: Long):String{
    if(amount<0){
        return "-¥${NumberFormat.getNumberInstance(Locale.JAPAN).format(amount.absoluteValue)}"
    } else {
        return "¥${NumberFormat.getNumberInstance(Locale.JAPAN).format(amount)}"
    }
}
