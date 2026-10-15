package com.example.mywallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Balance()
{
    val application = LocalContext.current.applicationContext as MyWalletApplication
    val dao = application.database.transactionDao()
    val factory = TransactionViewModelFactory(dao)
    val viewModel: TransactionViewModel = viewModel(
        factory = factory
    )
    viewModel.showBalance()
    BalanceContent(balance = viewModel.balance)
}

@Composable
fun BalanceContent(modifier: Modifier = Modifier, balance: Long){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.size(30.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1D263A)),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "残高",
                    color = Color(0xFFDFF2FF),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "¥$balance",
                    color = Color(0xFFDFF2FF),
                    fontSize = 60.sp
                )
            }
        }
    }
}

@Preview (backgroundColor = 0xFF171824)
@Composable
fun TestBalance() {
    BalanceContent(balance = 1000000L)
}
