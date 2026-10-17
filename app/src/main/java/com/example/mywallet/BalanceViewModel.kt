package com.example.mywallet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywallet.data.TransactionDao
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.ZoneId

class BalanceViewModel(
    private val dao: TransactionDao
) : ViewModel(){
    var monthlyTotal by mutableLongStateOf(0L)
       private set

    fun showMonthlyTotal(yearMonth: YearMonth){
        viewModelScope.launch{

            val zone = ZoneId.systemDefault()

            val start = yearMonth
                .atDay(1)
                .atStartOfDay(zone)
                .toInstant()
                .toEpochMilli()

            val end = yearMonth
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay(zone)
                .toInstant()
                .toEpochMilli()

            monthlyTotal = dao.getTotalBetween(start,end) ?:0L
        }
    }
}