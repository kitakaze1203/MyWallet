package com.example.mywallet.transaction

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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

    var expenseLabelId by mutableStateOf<Long?>(null)
        private set

    var grossLabelId by mutableStateOf<Long?>(null)
        private set

    fun loadDefaultLabel(){
        viewModelScope.launch{
            expenseLabelId = dao.getExpenseDefault()
            grossLabelId = dao.getGrossDefault()
        }
    }
    fun addTransaction(amount: Long,labelId: Long?) {
        viewModelScope.launch {
            dao.insertTransaction(
                Transaction(
                    amount = amount,
                    timestamp = System.currentTimeMillis(),
                    labelId = labelId
                )
            )
        }
    }
}