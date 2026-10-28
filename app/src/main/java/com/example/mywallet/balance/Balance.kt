package com.example.mywallet.balance

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywallet.MyWalletApplication
import com.example.mywallet.module.formatAmount
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceContent(modifier: Modifier = Modifier,
                   balance: Long,
                   monthlyTotal: Long,
                   monthlyGross: Long,
                   monthlyExpense: Long){

    var expanded by remember{ mutableStateOf(false) }
    var selected by remember{ mutableStateOf("ラベル別")}
    val interactionSource = remember { MutableInteractionSource() }

    val options = listOf("食費","買い物","推し活","調整","その他")

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
                    fontSize = 40.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = formatAmount(balance),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDFF2FF),
                    textAlign = TextAlign.End
                )
            }
        }

        Spacer(modifier = Modifier.size(120.dp))
        Category(text = "月間収支", amount = monthlyTotal, icon = Icons.Default.CalendarMonth)

        Spacer(modifier = Modifier.size(40.dp))
        Category(text = "今月の支出", amount = monthlyExpense, icon = Icons.Default.Payment)

        Spacer(modifier = Modifier.size(40.dp))
        Category(text = "今月の収入", amount = monthlyGross, icon = Icons.Default.AttachMoney)

        Spacer(modifier = Modifier.size(40.dp))

        Row(
            modifier = modifier
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
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "ラベル別",
                    tint = Color(0xFFDFF2FF)
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {expanded = it},
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp)
                ){
                    BasicTextField(
                        value = selected,
                        onValueChange = {selected = it},
                        readOnly = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 20.sp,
                            color = Color(0xFFDFF2FF),
                        ),
                        singleLine = true,
                        interactionSource = interactionSource,
                        modifier = Modifier
                            .fillMaxSize()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        decorationBox = { innerTextField ->
                            TextFieldDefaults.DecorationBox(
                                value = selected,
                                innerTextField = innerTextField,
                                enabled = true,
                                singleLine = true,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = interactionSource,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded, modifier = modifier.size(40.dp))},
                                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                                    top = 0.dp,
                                    bottom = 0.dp,
                                    start = 5.dp,
                                    end = 0.dp
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTrailingIconColor = Color(0xFFDFF2FF),
                                    unfocusedTrailingIconColor = Color(0xFFDFF2FF),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                )
                            )
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                        containerColor = Color(0xFF25283A)
                    ) {
                        options.forEach { option ->

                            val isSelected = option == selected

                            DropdownMenuItem(
                                text = { Text(
                                    text = option,
                                    color = if(isSelected) Color.White else Color(0xFFE5EBF8)
                                )},
                                onClick = {
                                    selected = option
                                    expanded = false
                                },
                                modifier = Modifier.background(
                                    color = if(isSelected) Color(0xFF465581) else Color(0xFF25283A)
                                )
                            )
                        }
                    }

                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 30.sp,
                    text = "¥$monthlyGross",
                    color = if(monthlyGross<0) Color(0xFFFF6B6B) else Color(0xFF65D69A),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun Category(modifier:Modifier = Modifier, text:String, amount:Long, icon: ImageVector){
    Row(
        modifier = modifier
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
                imageVector = icon,
                contentDescription = text,
                tint = Color(0xFFDFF2FF)
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                fontSize = 20.sp,
                text = text,
                color = Color(0xFFDFF2FF)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontSize = 30.sp,
                text = formatAmount(amount),
                color = if(amount<0) Color(0xFFFF6B6B) else Color(0xFF65D69A),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview (backgroundColor = 0xFF171824)
@Composable
fun TestBalance() {
    BalanceContent(
        balance = 1000000L,
        monthlyTotal = 10000L,
        monthlyExpense = -10000L,
        monthlyGross = 10000L)
}
