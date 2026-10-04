package com.example.mywallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywallet.ui.theme.MywalletTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MywalletTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TransactionInput(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionInput(modifier: Modifier = Modifier) {
    var amount by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                color = Color.White,
                fontSize = 80.sp,
                text = "¥" + amount.toString()
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RowButton(textFirst = "7", textSecond = "8", textThird = "9", amount = amount, amountChange = {amount = it})
            Spacer(modifier = Modifier.size(10.dp))
            RowButton(textFirst = "4", textSecond = "5", textThird = "6", amount = amount, amountChange = {amount = it})
            Spacer(modifier = Modifier.size(10.dp))
            RowButton(textFirst = "1", textSecond = "2", textThird = "3", amount = amount, amountChange = {amount = it})
            Spacer(modifier = Modifier.size(10.dp))
            RowButton(textFirst = "0", textSecond = "00", textThird = "⇐", amount = amount, amountChange = {amount = it})
        }
    }
}

@Composable
fun RowButton(
    modifier: Modifier = Modifier,
    textFirst: String,
    textSecond: String,
    textThird: String,
    amount: Int,
    amountChange: (Int)->Unit)
{
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    )
    {
        Buttons(text = textFirst, amount = amount, amountChange = amountChange)
        Buttons(text = textSecond, amount = amount, amountChange = amountChange)
        Buttons(text = textThird, amount = amount, amountChange = amountChange)
    }
}

@Composable
fun Buttons(modifier: Modifier = Modifier,text: String,amount: Int,amountChange: (Int)->Unit)
{
    val targetText = stringResource(R.string.delete)
    Button(
        modifier = Modifier
            .size(100.dp),
        onClick = {
            if(text == "00"){
                amountChange(amount * 100)
            }else if(text != targetText){
                amountChange(amount * 10 + text.toInt())
            }else {
                amountChange(amount / 10)
            }
        },
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    ) {
        if (text == targetText) {
            Icon(
                painter = painterResource(id = R.drawable.outline_arrow_back_24),
                contentDescription = "戻る",
                modifier = Modifier.size(40.dp)
            )
        } else {
            Text(
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                text = text,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun test()
{
    Buttons(text = stringResource(R.string.delete),amount = 0, amountChange = {})
}