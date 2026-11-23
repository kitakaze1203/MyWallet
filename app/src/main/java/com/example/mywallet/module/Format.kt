package com.example.mywallet.module

import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale
import kotlin.math.absoluteValue

fun formatAmount(amount: Long):String{
    if(amount<0){
        return "-¥${NumberFormat.getNumberInstance(Locale.JAPAN).format(amount.absoluteValue)}"
    } else {
        return "¥${NumberFormat.getNumberInstance(Locale.JAPAN).format(amount)}"
    }
}

fun formatDateString(millis: Long?):String{
    val date = millis?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    return date?.let { "${it.year}/${it.monthValue}/${it.dayOfMonth}" } ?: ""
}
