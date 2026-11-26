package com.example.mywallet.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywallet.MyWalletApplication
import com.example.mywallet.data.Label
import com.example.mywallet.module.formatAmount
import com.example.mywallet.module.formatDateString
import com.example.mywallet.module.setPaymentDay

enum class Screen {
    MAIN,
    LABEL,
    SUBSCRIBE
}

enum class Cycle(val code:String){
    YEAR("年毎"),
    MONTH("ヶ月毎"),
    DAY("日毎")
}

data class Subscribe(
    var basePaymentDay: Long,
    var nextPaymentDay: Long,
    val name: String,
    val amount: Long,
    val cycle: Cycle,
    val value: Long,
)

@Composable
fun Setting(modifier: Modifier = Modifier) {
    var screen by remember { mutableStateOf(Screen.MAIN) }

    val application = LocalContext.current.applicationContext as MyWalletApplication
    val dao = application.database.transactionDao()
    val factory = SettingViewModelFactory(dao)
    val viewModel: SettingViewModel = viewModel(
        factory = factory
    )

    val labels by viewModel.labels.collectAsStateWithLifecycle()

    BackHandler(
        enabled = (screen != Screen.MAIN)
    ) {
        screen = Screen.MAIN
    }

    when (screen) {
        Screen.MAIN -> {
            MainScreen(onLabelClick = { screen = Screen.LABEL }, onSubscribeClick = { screen = Screen.SUBSCRIBE })
        }

        Screen.LABEL -> {
            LabelSetting(
                labels = labels.sortedByDescending { it.id },
                addLabel = viewModel::addLabel,
                deleteLabel = viewModel::deleteLabel,
                setDefault = viewModel::setDefault,
                clearDefault = viewModel::clearDefault
            )
        }

        Screen.SUBSCRIBE -> {
            SubscribeSetting()
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, onLabelClick: () -> Unit, onSubscribeClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            fontSize = 60.sp,
            text = "管理",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFDFF2FF)
        )

        Spacer(modifier = Modifier.size(100.dp))

        Button(

            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
                .weight(1f),
            onClick = onLabelClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonColors(
                containerColor = Color(0xFF2A4373),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF2A4373),
                disabledContentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                fontSize = 50.sp,
                text = "ラベル管理",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFDFF2FF)
            )
        }

        Spacer(modifier = Modifier.weight(0.3f))

        Button(
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
                .weight(1f),
            onClick = onSubscribeClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonColors(
                containerColor = Color(0xFF2A4373),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF2A4373),
                disabledContentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                fontSize = 50.sp,
                text = "サブスク管理",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFDFF2FF)
            )
        }

        Spacer(modifier = Modifier.size(100.dp))
    }
}

@Composable
fun LabelSetting(
    modifier: Modifier = Modifier,
    labels: List<Label>,
    addLabel: (String) -> Unit,
    deleteLabel: (Label) -> Unit,
    setDefault: (Int, String) -> Unit,
    clearDefault: (Int, String) -> Unit
) {

    var newLabel by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(30.dp)) {

        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                fontSize = 60.sp,
                text = "ラベル管理",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFDFF2FF)
            )

            Spacer(modifier = modifier.size(50.dp))

            if (newLabel) {
                NewLabel(addLabel = {
                    addLabel(it)
                    newLabel = false
                })
                Spacer(modifier = Modifier.padding(10.dp))
            }

            LazyColumn(modifier = modifier) {
                items(items = labels) { label ->
                    LabelCard(
                        modifier = modifier.padding(vertical = 10.dp),
                        label = label,
                        deleteLabel = { deleteLabel(label) },
                        setDefault = setDefault,
                        clearDefault = clearDefault
                    )
                }
            }
        }

        Button(
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding()
                .size(70.dp),
            onClick = { newLabel = true },
            shape = CircleShape,
            colors = ButtonColors(
                containerColor = Color(0xFF2A4373),
                contentColor = Color(0xFFDFF2FF),
                disabledContainerColor = Color(0xFF2A4373),
                disabledContentColor = Color(0xFFDFF2FF)
            ),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp,
                pressedElevation = 5.dp,
            )
        ) {
            Text(
                fontSize = 50.sp,
                text = "+",
            )
        }
    }
}

@Composable
fun NewLabel(modifier: Modifier = Modifier, addLabel: (String) -> Unit) {

    var labelName by remember { mutableStateOf("") }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D365A),
            contentColor = Color(0xFFDFF2FF)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = labelName,
                onValueChange = { labelName = it },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color(0xFFDFF2FF),
                    unfocusedTextColor = Color(0xFFDFF2FF),
                    focusedContainerColor = Color(0xFF2D365A),
                    unfocusedContainerColor = Color(0xFF2D365A),
                    focusedIndicatorColor = Color(0xFFDFF2FF),
                    unfocusedIndicatorColor = Color(0xFFDFF2FF)
                ),
                textStyle = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                placeholder = {
                    Text(
                        text = "名前を入力",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0x80DFF2FF)
                    )
                }
            )

            IconButton(
                modifier = Modifier.size(50.dp),
                onClick = { if (labelName!="") addLabel(labelName) },
                enabled = labelName!="",
                colors = IconButtonColors(
                    containerColor = Color(0xFF355487),
                    contentColor = Color(0xFF65D69A),
                    disabledContainerColor = Color(0xFF292E46),
                    disabledContentColor = Color(0xFF69728A)
                )
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = "確定"
                )
            }

        }
    }
}

@Composable
fun LabelCard(
    modifier: Modifier = Modifier,
    label: Label,
    deleteLabel: () -> Unit,
    setDefault: (Int, String) -> Unit,
    clearDefault: (Int, String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D365A),
            contentColor = Color(0xFFDFF2FF)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = label.labelName,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (label.grossDefault) {
                    Box(
                        modifier = modifier
                            .padding(end = 20.dp)
                            .height(40.dp)
                            .width(55.dp)
                            .background(
                                color = Color(0xFF29443A),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "収入",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF80CFA5)
                        )
                    }
                } else if (label.expenseDefault) {
                    Box(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .height(40.dp)
                            .width(55.dp)
                            .background(
                                color = Color(0xFF473039),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "支出",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF28B94)
                        )
                    }
                }
                Box {
                    IconButton(
                        modifier = Modifier.size(45.dp),
                        onClick = { expanded = !expanded },
                        colors = IconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFFDFF2FF),
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color(0xFFDFF2FF)
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "ラベル設定メニュー"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = Color(0xFF3A4770)
                    ) {
                        if (!label.noDelete) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "ラベルを削除",
                                        color = Color(0xFFDFF2FF)
                                    )
                                },
                                onClick = {
                                    deleteLabel()
                                    expanded = false
                                }
                            )
                        }
                        if (label.expenseDefault) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "支出デフォルトを解除",
                                        color = Color(0xFFDFF2FF)
                                    )
                                },
                                onClick = {
                                    clearDefault(1, label.labelName)
                                    expanded = false
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "支出デフォルトに設定",
                                        color = Color(0xFFDFF2FF)
                                    )
                                },
                                onClick = {
                                    setDefault(1, label.labelName)
                                    expanded = false
                                }
                            )
                        }
                        if (label.grossDefault) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "収入デフォルトを解除",
                                        color = Color(0xFFDFF2FF)
                                    )
                                },
                                onClick = {
                                    clearDefault(0, label.labelName)
                                    expanded = false
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "収入デフォルトに設定",
                                        color = Color(0xFFDFF2FF)
                                    )
                                },
                                onClick = {
                                    setDefault(0, label.labelName)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubscribeSetting(modifier: Modifier = Modifier) {
    val subs = remember {
        mutableStateListOf<Subscribe>(
            Subscribe(
                amount = 1000,
                basePaymentDay = 1794672660000L,
                nextPaymentDay = 1797264660000L,
                name = "YouTube",
                cycle = Cycle.MONTH,
                value = 1
            ),
            Subscribe(
                amount = 700,
                basePaymentDay = 1794759060000L,
                nextPaymentDay = 1795882260000L,
                name = "YouTubeMusic",
                cycle = Cycle.DAY,
                value = 14
            )
        )
    }

    var newSubscribe by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(30.dp)) {

        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                fontSize = 55.sp,
                text = "サブスク管理",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFDFF2FF)
            )

            Spacer(modifier = Modifier.size(50.dp))

            if (newSubscribe) {
                NewSubscribe(addSubscribe = { name,amount,date,value,cycle ->
                    subs.add(0,
                        Subscribe(
                            name = name,
                            amount = amount,
                            basePaymentDay = date,
                            nextPaymentDay = setPaymentDay(millis = date, baseMillis = date, cycle = cycle, value = value),
                            value = value,
                            cycle = cycle))
                    newSubscribe = false
                })
                Spacer(modifier = Modifier.padding(10.dp))
            }

            LazyColumn(modifier = modifier) {
                items(items = subs) { sub ->
                    SubscribeCard(
                        modifier = modifier.padding(vertical = 10.dp),
                        sub = sub,
                        deleteSubscribe = {subs.remove(sub)}
                    )
                }
            }
        }

        Button(
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding()
                .size(70.dp),
            onClick = { newSubscribe = true },
            shape = CircleShape,
            colors = ButtonColors(
                containerColor = Color(0xFF2A4373),
                contentColor = Color(0xFFDFF2FF),
                disabledContainerColor = Color(0xFF2A4373),
                disabledContentColor = Color(0xFFDFF2FF)
            ),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp,
                pressedElevation = 5.dp,
            )
        ) {
            Text(
                fontSize = 50.sp,
                text = "+",
            )
        }
    }
}

@Composable
fun SubscribeCard(modifier: Modifier = Modifier, sub: Subscribe, deleteSubscribe: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D365A),
            contentColor = Color(0xFFDFF2FF)
        )
    ) {
        Column {

            Spacer(modifier = Modifier.weight(0.1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = sub.name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Box {
                    IconButton(
                        modifier = Modifier.size(45.dp),
                        onClick = { expanded = !expanded },
                        colors = IconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFFDFF2FF),
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color(0xFFDFF2FF)
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "サブスク設定メニュー"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = Color(0xFF3A4770)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "ラベルを削除",
                                    color = Color(0xFFDFF2FF)
                                )
                            },
                            onClick = {
                                deleteSubscribe()
                                expanded = !expanded
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "金額： ${formatAmount(sub.amount)}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "予定： ${formatDateString(sub.nextPaymentDay)}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "更新： ${sub.value}${sub.cycle.code}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSubscribe(modifier: Modifier = Modifier, addSubscribe: (String, Long, Long, Long, Cycle)->Unit) {

    var subscribeName by remember { mutableStateOf("") }
    var subscribeAmount by remember { mutableStateOf("") }
    var subscribeCycle by remember { mutableStateOf<Cycle>(Cycle.MONTH) }
    var subscribeDate by remember { mutableLongStateOf(0L) }
    var subscribeValue by remember { mutableStateOf("")}

    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("月ごと") }
    var showDatePicker by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val date = rememberDatePickerState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D365A),
            contentColor = Color(0xFFDFF2FF)
        )
    ) {
        Column(modifier = modifier.padding(bottom = 10.dp)) {

                TextField(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(horizontal = 15.dp)
                        .weight(1f),

                    value = subscribeName,
                    onValueChange = { subscribeName = it },

                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFFDFF2FF),
                        unfocusedTextColor = Color(0xFFDFF2FF),
                        focusedContainerColor = Color(0xFF2D365A),
                        unfocusedContainerColor = Color(0xFF2D365A),
                        focusedIndicatorColor = Color(0xFFDFF2FF),
                        unfocusedIndicatorColor = Color(0xFFDFF2FF)
                    ),
                    textStyle = TextStyle(
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 23.sp
                    ),
                    placeholder = {
                        Text(
                            text = "名前を入力",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0x80DFF2FF)
                        )
                    }
                )

                TextField(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(horizontal = 15.dp)
                        .weight(1f),
                    value = subscribeAmount,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { subscribeAmount = it },

                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFFDFF2FF),
                        unfocusedTextColor = Color(0xFFDFF2FF),
                        focusedContainerColor = Color(0xFF2D365A),
                        unfocusedContainerColor = Color(0xFF2D365A),
                        focusedIndicatorColor = Color(0xFFDFF2FF),
                        unfocusedIndicatorColor = Color(0xFFDFF2FF)
                    ),
                    textStyle = TextStyle(
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 23.sp
                    ),
                    placeholder = {
                        Text(
                            text = "金額を入力",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0x80DFF2FF)
                        )
                    }
                )

            Box (modifier = Modifier
                .padding(horizontal = 15.dp)
                .width(300.dp)
                .weight(1f)
            ){
                TextField(
                    value = formatDateString(date.selectedDateMillis),
                    onValueChange = {},
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFFDFF2FF),
                        unfocusedTextColor = Color(0xFFDFF2FF),
                        focusedContainerColor = Color(0xFF2D365A),
                        unfocusedContainerColor = Color(0xFF2D365A),
                        focusedIndicatorColor = Color(0xFFDFF2FF),
                        unfocusedIndicatorColor = Color(0xFFDFF2FF)
                    ),
                    textStyle = TextStyle(
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 23.sp
                    ),
                    placeholder = {
                        Text(
                            text = "前回の支払日を選択",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0x80DFF2FF)
                        )
                    }
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                ) {}
            }


            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                                subscribeDate = date.selectedDateMillis ?: 0L
                            }
                        ) {
                            Text("決定")
                        }
                    }
                ) {
                    DatePicker(
                        state = date
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        modifier = Modifier.width(80.dp),
                        value = subscribeValue,
                        onValueChange = { subscribeValue = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color(0xFFDFF2FF),
                            unfocusedTextColor = Color(0xFFDFF2FF),
                            focusedContainerColor = Color(0xFF2D365A),
                            unfocusedContainerColor = Color(0xFF2D365A),
                            focusedIndicatorColor = Color(0xFFDFF2FF),
                            unfocusedIndicatorColor = Color(0xFFDFF2FF)
                        ),
                        textStyle = TextStyle(
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 23.sp
                        ),
                        placeholder = {
                            Text(
                                text = "数値",
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0x80DFF2FF)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        modifier = Modifier
                            .width(100.dp)
                            .height(45.dp)
                    ) {
                        BasicTextField(
                            value = selected,
                            onValueChange = {},
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
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expanded
                                        )
                                    },
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
                            onDismissRequest = { expanded = false },
                            containerColor = Color(0xFF25283A)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "年ごと",
                                        color = Color(0xFFE5EBF8)
                                    )
                                },
                                onClick = {
                                    selected = "年ごと"
                                    expanded = false
                                    subscribeCycle = Cycle.YEAR
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "月ごと",
                                        color = Color(0xFFE5EBF8)
                                    )
                                },
                                onClick = {
                                    selected = "月ごと"
                                    expanded = false
                                    subscribeCycle = Cycle.MONTH
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "日ごと",
                                        color = Color(0xFFE5EBF8)
                                    )
                                },
                                onClick = {
                                    selected = "日ごと"
                                    expanded = false
                                    subscribeCycle = Cycle.DAY
                                }
                            )
                        }
                    }
                }

                IconButton(
                    modifier = Modifier.size(50.dp),
                    onClick = { addSubscribe(subscribeName, subscribeAmount.toLong(), subscribeDate, subscribeValue.toLong(), subscribeCycle) },
                    enabled = subscribeName != "" && subscribeAmount != "" && (subscribeValue.toLongOrNull() ?: -1L) > 0L && subscribeDate != 0L,
                    colors = IconButtonColors(
                        containerColor = Color(0xFF355487),
                        contentColor = Color(0xFF65D69A),
                        disabledContainerColor = Color(0xFF292E46),
                        disabledContentColor = Color(0xFF69728A)
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Default.Check,
                        contentDescription = "確定"
                    )
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xFF171824, showBackground = true)
@Composable
fun TestSetting() {
    SubscribeSetting()
}