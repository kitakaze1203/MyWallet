package com.example.mywallet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Transaction(modifier: Modifier = Modifier)
{
    val application = LocalContext.current.applicationContext as MyWalletApplication
    val dao = application.database.transactionDao()
    val factory = TransactionViewModelFactory(dao)
    val viewModel: TransactionViewModel = viewModel(
        factory = factory
    )
    TransactionContent{
        viewModel.addTransaction(it)
    }
}

@Composable
fun TransactionContent(modifier: Modifier = Modifier, addTransaction: (Long)->Unit) {
    var amount by remember { mutableIntStateOf(0) }
    var incomeAlpha by remember { mutableFloatStateOf(1f) }
    var expenditureAlpha by remember { mutableFloatStateOf(1f) }
    var amountColor by remember { mutableStateOf(Color(0xFFDFF2FF)) }
    val amountAlpha = remember { Animatable(1f) }
    val animateOffset = remember { Animatable(0f) }
    var swipeOffset by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    var isDrag by remember { mutableStateOf(true) }

    SwipeArea(
        modifier = modifier,
        onSwipeUp = {
            amount = -amount
            isDrag = false
            addTransaction(amount.toLong())
            scope.launch {
                animateOffset.snapTo(swipeOffset)
                launch {
                    animateOffset.animateTo(
                        -500f,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                    animateOffset.snapTo(0f)
                    swipeOffset = 0f
                    amountColor = Color(0xFFDFF2FF)
                    amountAlpha.snapTo(1f)
                    expenditureAlpha = 1f
                    amount = 0
                    isDrag = true
                }
                launch {
                    amountAlpha.animateTo(
                        0f,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                }
            }
        },
        onSwipeDown = {
            isDrag = false
            addTransaction(amount.toLong())
            scope.launch {
                animateOffset.snapTo(swipeOffset)
                launch {
                    animateOffset.animateTo(
                        500f,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                    animateOffset.snapTo(0f)
                    swipeOffset = 0f
                    amountColor = Color(0xFFDFF2FF)
                    amountAlpha.snapTo(1f)
                    incomeAlpha = 1f
                    amount = 0
                    isDrag = true
                }
                launch {
                    amountAlpha.animateTo(
                        0f,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearEasing
                        )
                    )
                }
            }
        },
        onSwipeEnd = {
            swipeOffset = 0f
            incomeAlpha = 1f
            expenditureAlpha = 1f
        },
        onDragDown = { amount, color ->
            amountColor = color
            swipeOffset = amount
            incomeAlpha = -amount/200f+1
            expenditureAlpha = amount/200f+1
        },
        onDragUp = { amount, color ->
            amountColor = color
            swipeOffset = amount
            incomeAlpha = -amount/200f+1
            expenditureAlpha = amount/200f+1
        },
        onDragCancel = {
            amount =0
            incomeAlpha = 1f
            expenditureAlpha = 1f
            swipeOffset = 0f
            amountColor = Color(0xFFDFF2FF)
        }
    ){
        val amountOffset = if(isDrag) swipeOffset else animateOffset.value

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(30.dp),){
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically),
                    color = Color(0xAADFF2FF).copy(alpha = expenditureAlpha),
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
                    color = amountColor.copy(alpha = amountAlpha.value),
                    fontSize = 80.sp,
                    textAlign = TextAlign.End,
                    lineHeight = 1.em,
                    text = "¥$amount"
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically),
                    color = Color(0xDFF2FF).copy(alpha = incomeAlpha),
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 1.em,
                    text = "↑スワイプで出金"
                )
            }
            Column(
                modifier = Modifier
                    .weight(1.4f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RowButton(textFirst = "7", textSecond = "8", textThird = "9", amount = amount, amountChange = {amount = it})
                Spacer(modifier = Modifier.size(10.dp))
                RowButton(textFirst = "4", textSecond = "5", textThird = "6", amount = amount, amountChange = {amount = it})
                Spacer(modifier = Modifier.size(10.dp))
                RowButton(textFirst = "1", textSecond = "2", textThird = "3", amount = amount, amountChange = {amount = it})
                Spacer(modifier = Modifier.size(10.dp))
                RowButton(textFirst = "0", textSecond = "00", textThird = "⇐", amount = amount, amountChange = {amount = it})
                Spacer(modifier = Modifier.size(10.dp))
            }
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
    Row(modifier = modifier.fillMaxWidth(),
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
        modifier = modifier
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
                color = Color(0xFFDFF2FF),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}


@Preview(backgroundColor = 0xFF171824)
@Composable
fun Test()
{
    TransactionContent { }
}