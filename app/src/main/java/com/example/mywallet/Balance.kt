package com.example.mywallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun Balance(){
    val application = LocalContext.current.applicationContext as MyWalletApplication
    val dao = application.database.transactionDao()
    val factory = TransactionViewModelFactory(dao)
    val viewModel: TransactionViewModel = viewModel(
        factory = factory
    )
    viewModel.showBalance()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text(
            text = "¥" + viewModel.balance,
            color = Color.White,
            fontSize = 100.sp
        )
    }
}