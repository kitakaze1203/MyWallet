package com.example.mywallet

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mywallet.data.AppDatabase

class MyWalletApplication : Application() {

    val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "my_wallet_database"
        )
            .addCallback(object : RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    db.execSQL("""
                        INSERT INTO `Label` (labelName, expenseDefault, grossDefault, noDelete)
                        VALUES 
                            ('調整', 0, 0, 1),
                            ('サブスク', 0, 0, 1)
                    """.trimIndent()
                    )
                }
            })
            .build()
    }
}