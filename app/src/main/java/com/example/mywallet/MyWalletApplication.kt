package com.example.mywallet

import android.app.Application
import androidx.room.Room
import com.example.mywallet.data.AppDatabase

class MyWalletApplication : Application() {

    val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "my_wallet_database"
        ).build()
    }
}