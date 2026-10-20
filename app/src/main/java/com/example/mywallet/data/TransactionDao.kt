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

    @Query("""
        SELECT SUM(amount)
        FROM `Transaction`
        WHERE timestamp >= :start
        AND timestamp < :end""")
    suspend fun getTotalBetween(
        start: Long,
        end: Long
    ): Long?

    @Query("""
        SELECT SUM(amount)
        FROM `Transaction`
        WHERE amount<0
        AND timestamp >= :start
        AND timestamp < :end
    """)
    suspend fun getExpenseTotalBetween(
        start: Long,
        end: Long
    ): Long?

    @Query("""
        SELECT SUM(amount)
        FROM `Transaction`
        WHERE amount>0
        AND timestamp >= :start
        AND timestamp < :end
    """)
    suspend fun getGrossTotalBetween(
        start: Long,
        end: Long
    ): Long?

    @Query("""
        SELECT * FROM `Transaction`
        ORDER BY timestamp DESC
    """)
    suspend fun getAll(): List<Transaction>
}