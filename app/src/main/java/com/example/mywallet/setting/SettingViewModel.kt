package com.example.mywallet.setting


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywallet.data.Label
import com.example.mywallet.data.TransactionDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val dao: TransactionDao
) : ViewModel() {

    val labels: StateFlow<List<Label>> = dao.getAllLabels()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addLabel(name: String){
        viewModelScope.launch{
            dao.insertLabel(
                Label(
                    labelName = name
                )
            )
        }
    }

    fun deleteLabel(label: Label){
        viewModelScope.launch{
            dao.deleteTransactionLabel(
                labelId = label.id
            )
            dao.deleteLabel(
                label = label
            )
        }
    }

    /**
     * 支出デフォルトを割り当てる関数
     *
     * 一つをデフォルトに設定すると他のラベルはデフォルトから外れるようになっている
     *
     * @target 0にするとGrossDefaultが　1にするとExpanseDefaultがtrueになる
     *
     * @labelName 対象のlabelName
     */
    fun setDefault(target: Int, labelName: String){
        viewModelScope.launch {
            if(target == 0) dao.setGrossDefault(labelName)
            else dao.setExpenseDefault(labelName)
        }
    }

    /**
     * 支出デフォルトを解除する関数
     *
     * 別にデフォルトがなくてもいいのでfalseになるだけ
     *
     * @target 0にするとGrossDefaultが　1にするとExpanseDefaultがfalseになる
     *
     * @labelName 対象のlabelName
     */
    fun clearDefault(target:Int, labelName: String){
        viewModelScope.launch {
            if(target == 0) dao.clearGrossDefault(labelName)
            else dao.clearExpenseDefault(labelName)
        }
    }

}