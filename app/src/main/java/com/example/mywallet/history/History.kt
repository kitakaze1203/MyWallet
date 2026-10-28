package com.example.mywallet.history

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywallet.MyWalletApplication
import com.example.mywallet.data.Transaction
import com.example.mywallet.module.formatAmount
import java.time.Instant
import java.time.ZoneId
@Composable
fun History(){
    val application = LocalContext.current.applicationContext as MyWalletApplication
    val dao = application.database.transactionDao()
    val factory = HistoryViewModelFactory(dao)
    val viewModel: HistoryViewModel = viewModel(
        factory = factory
    )

    viewModel.getTransactions()

    HistoryContent(transactions = viewModel.transactions, onSetLabel = viewModel::setLabel)
}

@Composable
fun HistoryContent(
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
    onSetLabel: (String, Long) -> Unit
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
                    modifier = Modifier.padding(10.dp),
                    onSetLabel = onSetLabel
                )
            }
        }

    }
}

@Composable
fun HistoryCard(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onSetLabel: (String, Long) -> Unit
){

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
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Label(transaction = transaction, onSetLabel = onSetLabel)
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = formatAmount(amount = transaction.amount),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = if(transaction.amount<0) Color(0xFFFF6B6B) else Color(0xFF65D69A)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Label(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onSetLabel: (String, Long) -> Unit
){

    var expanded by remember{ mutableStateOf(false) }
    var selected by remember{ mutableStateOf(transaction.label ?: "ラベルなし")}
    val interactionSource = remember { MutableInteractionSource() }


    val options = listOf("食費","買い物","推し活","調整","その他")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = it},
        modifier = Modifier
            .width(120.dp)
            .height(40.dp)
    ) {
        BasicTextField(
            value = selected,
            onValueChange = {onSetLabel(it,transaction.id)},
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 13.sp,
                color = Color(0xFFDFF2FF),
                fontWeight = FontWeight.SemiBold
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
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                    contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = 5.dp,
                        end = 0.dp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF3A4770),
                        unfocusedContainerColor = Color(0xFF3A4770),
                        focusedTrailingIconColor = Color(0xFF8796BA),
                        unfocusedTrailingIconColor = Color(0xFF8796BA),
                        focusedIndicatorColor = Color(0xFF3A4770),
                        unfocusedIndicatorColor = Color(0xFF3A4770)
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
}

@Preview (backgroundColor = 0xFF171824)
@Composable
fun HistoryTest(){
    val dummyTransactions = listOf(
        Transaction(
            id = 1,
            amount = -1000L,
            timestamp = 1760972400000L,
            label = null
        ),
        Transaction(
            id = 2,
            amount = 3000L,
            timestamp = 1761058800000L,
            label = null
        ),
        Transaction(
            id = 3,
            amount = -500L,
            timestamp = 1761145200000L,
            label = null
        ),
        Transaction(
            id = 4,
            amount = -500L,
            timestamp = 1761145200000L,
            label = null
        ),
        Transaction(
            id = 5,
            amount = -500L,
            timestamp = 1761145200000L,
            label = null
        ),
        Transaction(
            id = 6,
            amount = -500L,
            timestamp = 1761145200000L,
            label = null
        ),
        Transaction(
            id = 7,
            amount = -500L,
            timestamp = 1761145200000L,
            label = null
        )
    )
    HistoryContent(transactions = dummyTransactions, onSetLabel = {label,id ->})
}