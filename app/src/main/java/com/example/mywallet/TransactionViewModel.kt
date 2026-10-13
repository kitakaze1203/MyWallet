package com.example.mywallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywallet.data.Transaction
import com.example.mywallet.data.TransactionDao
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val dao: TransactionDao
) : ViewModel() {

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
}