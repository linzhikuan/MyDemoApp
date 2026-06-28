package com.lzk.lettin.business.main.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lzk.lettin.business.main.data.model.LotteryDraw
import com.lzk.lettin.business.main.data.model.SavedTicket

@Database(
    entities = [LotteryDraw::class, SavedTicket::class],
    version = 1,
    exportSchema = false,
)
abstract class LotteryDatabase : RoomDatabase() {

    abstract fun lotteryDrawDao(): LotteryDrawDao
    abstract fun savedTicketDao(): SavedTicketDao

    companion object {
        @Volatile private var instance: LotteryDatabase? = null

        fun get(context: Context): LotteryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    LotteryDatabase::class.java,
                    "lottery.db",
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}
