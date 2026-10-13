package com.example.mywallet.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT SUM(amount) FROM `Transaction`")
    suspend fun getTotal(): Long?
}