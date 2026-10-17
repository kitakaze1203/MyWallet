package com.example.mywallet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywallet.data.Transaction
import com.example.mywallet.data.TransactionDao
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val dao: TransactionDao
) : ViewModel() {

    private var _balance by mutableStateOf(0L)
    private var _totalBetween by mutableStateOf(0L)
    val balance: Long
        get() = _balance

    val totalBetween: Long
        get() = _totalBetween
    fun addTransaction(amount: Long) {
        viewModelScope.launch {
            dao.insert(
                Transaction(
                    amount = amount,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun showBalance(){
        viewModelScope.launch {
            _balance = dao.getTotal() ?: 0L
        }
    }
}