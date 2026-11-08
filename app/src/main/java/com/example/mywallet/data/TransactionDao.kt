package com.example.mywallet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Insert
    suspend fun insertLabel(label: Label)

    @Delete
    suspend fun deleteLabel(label: Label)

    @Query("SELECT SUM(amount) FROM `Transaction`")
    suspend fun getTotal(): Long?

    @Query("""
        SELECT `Label`.id
        FROM `Label`
        WHERE `Label`.expenseDefault = 1
    """)
    suspend fun getExpenseDefault(): Long?

    @Query("""
        SELECT `Label`.id
        FROM `Label`
        WHERE `Label`.grossDefault = 1
    """)
    suspend fun getGrossDefault(): Long?

    @Query("""
    SELECT
        `Transaction`.*,
        `Label`.labelName
    FROM `Transaction`
    LEFT JOIN `Label`
        ON `Transaction`.labelId = Label.id
    ORDER BY `Transaction`.timestamp DESC
""")
    fun getAll(): Flow<List<TransactionWithLabel>>

    @Query("""
        SELECT SUM(`Transaction`.amount)
        FROM `Transaction`
        LEFT JOIN `Label`
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
        LEFT JOIN `Label`
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
        LEFT JOIN `Label`
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
    SELECT
        `Label`.*,
        SUM(`Transaction`.amount) AS amount
    FROM `Label`
    JOIN `Transaction`
        ON Label.id = `Transaction`.labelId
    GROUP BY `Label`.id
""")
    fun getLabelTotal(): Flow<List<LabelTotal>>

    @Query("SELECT * FROM Label")
    fun getAllLabels(): Flow<List<Label>>

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
        UPDATE `Transaction`
        SET labelId = NULL
        WHERE labelId = :labelId
    """)
    suspend fun deleteTransactionLabel(labelId: Long)

    @Query("""
        UPDATE `Label`
        SET expenseDefault = CASE labelname
                                WHEN :labelName THEN 1
                                ELSE 0
                                END
    """)
    suspend fun setExpenseDefault(labelName: String)

    @Query("""
        UPDATE `Label`
        SET expenseDefault = 0
        WHERE labelName = :labelName
    """)
    suspend fun clearExpenseDefault(labelName: String)

    @Query("""
        UPDATE `Label`
        SET grossDefault = CASE labelname
                                WHEN :labelName THEN 1
                                ELSE 0
                                END
    """)
    suspend fun setGrossDefault(labelName: String)

    @Query("""
        UPDATE `Label`
        SET grossDefault = 0
        WHERE labelName = :labelName
    """)
    suspend fun clearGrossDefault(labelName: String)

}