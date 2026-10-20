package com.example.mywallet.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mywallet.balance.BalanceViewModel
import com.example.mywallet.data.TransactionDao

class HistoryViewModelFactory(
    private val dao: TransactionDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        return HistoryViewModel(dao) as T
    }
}