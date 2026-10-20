package com.example.mywallet.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywallet.MyWalletApplication
import com.example.mywallet.balance.BalanceViewModel
import com.example.mywallet.balance.BalanceViewModelFactory
import com.example.mywallet.data.Transaction
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.days


val dummyTransactions = listOf(
    Transaction(
        id = 1,
        amount = -1000L,
        timestamp = 1760972400000L
    ),
    Transaction(
        id = 2,
        amount = 3000L,
        timestamp = 1761058800000L
    ),
    Transaction(
        id = 3,
        amount = -500L,
        timestamp = 1761145200000L
    ),
    Transaction(
        id = 4,
        amount = -500L,
        timestamp = 1761145200000L
    ),
    Transaction(
        id = 5,
        amount = -500L,
        timestamp = 1761145200000L
    ),
    Transaction(
        id = 6,
        amount = -500L,
        timestamp = 1761145200000L
    ),
    Transaction(
        id = 7,
        amount = -500L,
        timestamp = 1761145200000L
    )
)
@Composable
fun History(){
    val application = LocalContext.current.applicationContext as MyWalletApplication
    val dao = application.database.transactionDao()
    val factory = HistoryViewModelFactory(dao)
    val viewModel: HistoryViewModel = viewModel(
        factory = factory
    )

    viewModel.getTransactions()

    HistoryContent(transactions = viewModel.transactions)
}

@Composable
fun HistoryContent(
    modifier: Modifier = Modifier,
    transactions: List<Transaction>
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            fontSize = 80.sp,
            text = "使用履歴",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFDFF2FF)
        )

        Spacer(modifier = modifier.size(50.dp))

        LazyColumn(modifier = modifier) {
            items(transactions){ transaction ->
                HistoryCard(
                    transaction = transaction,
                    modifier = Modifier.padding(10.dp))
            }
        }

    }
}

@Composable
fun HistoryCard(modifier: Modifier = Modifier, transaction: Transaction){

    val date = Instant
        .ofEpochMilli(transaction.timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor =  Color(0xFF2D365A),
            contentColor = Color(0xFFDFF2FF)
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Text(
                text = "${date.monthValue}月${date.dayOfMonth}日",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.align(Alignment.Bottom),
                text = "¥${transaction.amount}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview (backgroundColor = 0xFF171824)
@Composable
fun Historytest(){
    HistoryContent(transactions = dummyTransactions)
}