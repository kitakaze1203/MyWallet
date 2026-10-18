package com.example.mywallet.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mywallet.data.TransactionDao

class TransactionViewModelFactory(
    private val dao: TransactionDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        return TransactionViewModel(dao) as T
    }
}