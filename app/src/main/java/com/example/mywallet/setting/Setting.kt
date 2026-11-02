package com.example.mywallet.setting

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywallet.history.HistoryCard

data class Label(
    var labelName: String,
    var expenseDefault: Boolean = false,
    var grossDefault: Boolean = false,
    var noDelete: Boolean = false,
)

enum class Screen {
    MAIN,
    LABEL
}
@Composable
fun Setting(modifier: Modifier = Modifier){
    var screen by remember { mutableStateOf(Screen.MAIN) }

    BackHandler(
        enabled = (screen != Screen.MAIN)
    ) {
        screen = Screen.MAIN
    }

    when(screen){
        Screen.MAIN -> {
            MainScreen( onLabelClick = { screen = Screen.LABEL})
        }

        Screen.LABEL -> {
            LabelSetting()
        }
    }
}
@Composable
fun MainScreen(modifier: Modifier = Modifier, onLabelClick:() -> Unit) {
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
            onClick = {},
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
fun LabelSetting(modifier: Modifier = Modifier) {

    var newLabel by remember { mutableStateOf(false) }

    val labels = remember{
        mutableStateListOf(
            Label(
                labelName = "調整",
                noDelete = true
            ),
            Label(
                labelName = "食費"
            )
        )
    }

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

            if(newLabel) {
                newLabel(addLabel = {
                    labels.add(index = 0, element = Label(labelName = it))
                    newLabel = false
                })
                Spacer(modifier = Modifier.padding(10.dp))
            }

            LazyColumn(modifier = modifier) {
                items(items = labels) { label ->
                    LabelCard(
                        modifier = modifier.padding(vertical = 10.dp),
                        label = label,
                        deleteLabel = { labels.remove(label) }
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
fun newLabel(modifier: Modifier = Modifier, addLabel:(String) -> Unit){

    var labelName by remember{mutableStateOf("")}
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
                onValueChange = {labelName = it},
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
                onClick = { addLabel(labelName) },
                colors = IconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF65D69A),
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color(0xFF65D69A)
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
    deleteLabel: () -> Unit
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
                fontWeight = FontWeight.Bold,
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
                                label.expenseDefault = false
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
                                label.expenseDefault = true
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
                                label.expenseDefault = false
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
                                label.grossDefault = true
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xFF171824, showBackground = true)
@Composable
fun TestSetting() {
    Setting()
}