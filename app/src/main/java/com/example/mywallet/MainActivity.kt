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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.mywallet.ui.theme.MywalletTheme
import kotlin.math.roundToInt

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
    var incomeWeight by remember { mutableFloatStateOf(1f) }
    var expenditureWeight by remember {mutableFloatStateOf(1f)}
    var amountOffset by remember {mutableFloatStateOf(0f)}
    var amountColor by remember { mutableStateOf(Color.White)}
    SwipeArea(
        modifier = modifier,
        onSwipeUp = {
            amount = -amount
            amountColor = Color.Red
        },
        onSwipeDown = {
            amountColor = Color.Green
        },
        onSwipeEnd = {
            amountOffset = 0f
        },
        onDragDown = { amountOffset = it },
        onDragUp = { amountOffset = it }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF171824))
    ) {
           Column(
               modifier = Modifier
               .weight(1f)
               .fillMaxSize()
               .padding(30.dp),){
               Text(
                   modifier = Modifier
                       .weight(expenditureWeight)
                       .fillMaxWidth()
                       .wrapContentHeight(Alignment.CenterVertically),
                   color = Color(0xAAFFFFFF),
                   fontSize = 25.sp,
                   textAlign = TextAlign.Center,
                   lineHeight = 1.em,
                   text = "↓スワイプで入金"
               )
               Text(
                   modifier = Modifier
                       .weight(2f)
                       .fillMaxWidth()
                       .wrapContentHeight(Alignment.CenterVertically)
                       .offset {
                           IntOffset(
                               x = 0,
                               y = amountOffset.roundToInt()
                           )
                       },
                   color = amountColor,
                   fontSize = 80.sp,
                   textAlign = TextAlign.End,
                   lineHeight = 1.em,
                   text = "¥" + amount.toString()
               )
               Text(
                   modifier = Modifier
                       .weight(incomeWeight)
                       .fillMaxWidth()
                       .wrapContentHeight(Alignment.CenterVertically),
                   color = Color(0xAAFFFFFF),
                   fontSize = 25.sp,
                   textAlign = TextAlign.Center,
                   lineHeight = 1.em,
                   text = "↑スワイプで出金"
                )
            }
        Column(
            modifier = Modifier
                .weight(1.2f)
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
            Spacer(modifier = Modifier.size(20.dp))
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
                if(amount * 100 < 999999) amountChange(amount * 100) else amountChange(999999)
            }else if(text != targetText){
                if(amount * 100 < 999999) amountChange(amount * 10 + text.toInt()) else amountChange(999999)
            }else {
                amountChange(amount / 10)
            }
        },
        shape = CircleShape,
        colors = ButtonColors(
            containerColor = Color(0xFF2A4373),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF2A4373),
            disabledContentColor = Color.White
        ),
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
    //Buttons(text = stringResource(R.string.delete),amount = 0, amountChange = {})
    TransactionInput()
}