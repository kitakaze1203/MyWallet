package com.example.mywallet.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywallet.data.Label
import com.example.mywallet.data.Transaction
import com.example.mywallet.data.TransactionDao
import com.example.mywallet.data.TransactionWithLabel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val dao: TransactionDao
) : ViewModel() {


    val transactions: StateFlow<List<TransactionWithLabel>> = dao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    val labels: StateFlow<List<Label>> = dao.getAllLabels()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    fun setLabel(labelId:Long, id:Long){
        viewModelScope.launch {
            dao.setLabel(labelId = labelId,id=id)
        }
    }

}