package com.example.mywallet.balance

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

    var monthlyGrossTotal by mutableLongStateOf(0L)
        private set

    var monthlyExpenseTotal by mutableLongStateOf(0L)
        private set

    var balance by mutableLongStateOf(0L)
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

    fun showMonthlyGrossTotal(yearMonth: YearMonth){
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

            monthlyGrossTotal = dao.getGrossTotalBetween(start,end) ?:0L
        }
    }

    fun showMonthlyExpenseTotal(yearMonth: YearMonth){
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

            monthlyExpenseTotal = dao.getExpenseTotalBetween(start,end) ?:0L
        }
    }

    fun showBalance(){
        viewModelScope.launch {
            balance = dao.getTotal() ?:0L
        }
    }
}