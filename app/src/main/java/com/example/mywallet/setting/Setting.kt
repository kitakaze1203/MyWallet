package com.example.mywallet.setting

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Label(
    val labelName: String,
    val expenseDefault: Boolean,
    val grossDefault: Boolean,
    val noDelete: Boolean,
)

@Composable
fun Setting(modifier: Modifier = Modifier){

    val labels = mutableListOf(Label(labelName = "調整", expenseDefault = false,grossDefault = false, noDelete = true))

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
                text = "ラベル管理",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFDFF2FF))
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
                color = Color(0xFFDFF2FF))
        }

        Spacer(modifier = Modifier.size(100.dp))
    }
}

@Composable
fun LabelSetting(modifier: Modifier = Modifier,labels: List<Label>){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            fontSize = 60.sp,
            text = "ラベル管理",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFDFF2FF)
        )
    }
}

@Composable
fun LabelCard(modifier: Modifier = Modifier,delete: ()->Unit,expDefault: ()->Unit,groDefault: ()->Unit){

    var expanded by remember { mutableStateOf(false) }

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
            verticalAlignment = Alignment.CenterVertically){

            Text(text = "ラベル１",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,)

            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "ラベル設定メニュー",
                        tint = Color(0xFFDFF2FF)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = Color(0xFF3A4770)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "ラベルを削除", color = Color(0xFFDFF2FF)) },
                        onClick = delete
                    )
                    DropdownMenuItem(
                        text = { Text(text = "支出デフォルトに設定", color = Color(0xFFDFF2FF)) },
                        onClick = expDefault
                    )
                    DropdownMenuItem(
                        text = { Text(text = "収入デフォルトに設定", color = Color(0xFFDFF2FF)) },
                        onClick = groDefault
                    )
                }
            }
        }
    }
}

@Preview (backgroundColor = 0xFF171824, showBackground = true)
@Composable
fun TestSetting(){
    LabelSetting(labels = listOf(Label("調整", expenseDefault = false, grossDefault = false, noDelete = true)))
    //LabelCard(delete = {}, expDefault = {}, groDefault = {})
}