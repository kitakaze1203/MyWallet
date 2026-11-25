package com.example.mywallet.module

import com.example.mywallet.setting.Cycle
import java.time.Instant
import java.time.ZoneId

fun setPaymentDay(millis:Long, baseMillis:Long, cycle: Cycle,value:Long):Long{
    var date = Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val baseDate = Instant.ofEpochMilli(baseMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()


    date = when(cycle){
        Cycle.YEAR -> date.plusYears(value)
        Cycle.MONTH -> date.plusMonths(value)
        Cycle.DAY -> date.plusDays(value)
    }

    if(cycle == Cycle.MONTH || cycle== Cycle.YEAR){
        val paymentDay = minOf(
            baseDate.dayOfMonth,
            date.lengthOfMonth()
        )

        date = date.withDayOfMonth(paymentDay)
    }


    val nextMillis = date
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()

    return nextMillis
}
