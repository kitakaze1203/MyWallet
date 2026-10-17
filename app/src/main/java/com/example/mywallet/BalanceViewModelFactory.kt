package com.example.mywallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mywallet.data.TransactionDao

class BalanceViewModelFactory(
    private val dao: TransactionDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        return BalanceViewModel(dao) as T
    }
}