package com.example.mywallet.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Insert
    suspend fun insertLabel(label: Label)

    @Query("SELECT SUM(amount) FROM `Transaction`")
    suspend fun getTotal(): Long?

    @Query("""
        SELECT SUM(`Transaction`.amount)
        FROM `Transaction`
        LEFT JOIN Label
            ON `Transaction`.labelId = `Label`.id
        WHERE timestamp >= :start
        AND timestamp < :end
        AND (`Label`.labelName != "調整" OR `Label`.labelName IS NULL)
        """)
    suspend fun getTotalBetween(
        start: Long,
        end: Long
    ): Long?

    @Query("""
        SELECT SUM(`Transaction`.amount)
        FROM `Transaction`
        LEFT JOIN Label
            ON `Transaction`.labelId = `Label`.id
        WHERE `Transaction`.amount<0
        AND timestamp >= :start
        AND timestamp < :end
        AND (`Label`.labelName != "調整" OR `Label`.labelName IS NULL)
    """)
    suspend fun getExpenseTotalBetween(
        start: Long,
        end: Long
    ): Long?

    @Query("""
        SELECT SUM(`Transaction`.amount)
        FROM `Transaction`
        LEFT JOIN Label
            ON `Transaction`.labelId = `Label`.id
        WHERE `Transaction`.amount>0
        AND timestamp >= :start
        AND timestamp < :end
        AND (`Label`.labelName != "調整" OR `Label`.labelName IS NULL)
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

    @Query("""
        UPDATE `Transaction`
        SET labelId = :labelId
        WHERE id = :id
    """)
    suspend fun setLabel(
        labelId: Long,
        id: Long
    )

    @Query("""
        SELECT SUM(amount) FROM `Transaction`
        LEFT JOIN Label
            ON `Transaction`.labelId = `Label`.id
        WHERE Label.labelName == :label
    """)
    suspend fun getLabelTotal(
        label: String,
    ): Long?
}