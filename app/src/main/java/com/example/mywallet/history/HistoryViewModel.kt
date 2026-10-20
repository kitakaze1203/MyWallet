package com.example.mywallet.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywallet.data.Transaction
import com.example.mywallet.data.TransactionDao
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val dao: TransactionDao
) : ViewModel() {

    var transactions by mutableStateOf<List<Transaction>>(emptyList())
        private set

    fun getTransactions() {
        viewModelScope.launch {
            transactions = dao.getAll()
        }
    }
}