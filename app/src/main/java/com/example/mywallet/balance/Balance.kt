package com.example.mywallet.balance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywallet.MyWalletApplication
import java.time.YearMonth

@Composable
fun Balance()
{
    val application = LocalContext.current.applicationContext as MyWalletApplication
    val dao = application.database.transactionDao()
    val factory = BalanceViewModelFactory(dao)
    val viewModel: BalanceViewModel = viewModel(
        factory = factory
    )
    viewModel.showBalance()
    viewModel.showMonthlyTotal(YearMonth.now())
    viewModel.showMonthlyGrossTotal(YearMonth.now())
    viewModel.showMonthlyExpenseTotal(YearMonth.now())
    BalanceContent(
        balance = viewModel.balance,
        monthlyTotal = viewModel.monthlyTotal,
        monthlyGross = viewModel.monthlyGrossTotal,
        monthlyExpense = viewModel.monthlyExpenseTotal)
}

@Composable
fun BalanceContent(modifier: Modifier = Modifier,
                   balance: Long,
                   monthlyTotal: Long,
                   monthlyGross: Long,
                   monthlyExpense: Long){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.size(80.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2D365A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(80.dp),
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "月間収支",
                    tint = Color(0xFFDFF2FF)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    fontSize = 30.sp,
                    text = "残高",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFDFF2FF)
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 45.sp,
                    text = "¥$balance",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDFF2FF),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.size(120.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2D365A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "月間収支",
                    tint = Color(0xFFDFF2FF)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    fontSize = 20.sp,
                    text = "月間収支",
                    color = Color(0xFFDFF2FF)
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 30.sp,
                    text = "¥$monthlyTotal",
                    color = Color(0xFFDFF2FF),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.size(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2D365A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Default.Payment,
                    contentDescription = "今月の支出",
                    tint = Color(0xFFDFF2FF)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    fontSize = 20.sp,
                    text = "今月の支出",
                    color = Color(0xFFDFF2FF)
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 30.sp,
                    text = "¥$monthlyExpense",
                    color = Color(0xFFDFF2FF),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.size(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2D365A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "今月の収入",
                    tint = Color(0xFFDFF2FF)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    fontSize = 20.sp,
                    text = "今月の収入",
                    color = Color(0xFFDFF2FF)
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 30.sp,
                    text = "¥$monthlyGross",
                    color = Color(0xFFDFF2FF),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Preview (backgroundColor = 0xFF171824)
@Composable
fun TestBalance() {
    BalanceContent(
        balance = 1000000L,
        monthlyTotal = 10000L,
        monthlyExpense = 10000L,
        monthlyGross = 10000L)
}
