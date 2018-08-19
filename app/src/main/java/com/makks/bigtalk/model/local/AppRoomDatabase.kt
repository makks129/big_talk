package com.makks.bigtalk.model.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.makks.bigtalk.global.App
import com.makks.bigtalk.model.domain.Question

@Database(
        entities = [
            Question::class],
        version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    companion object {
        fun build() = Room.databaseBuilder(App.context(),
                AppRoomDatabase::class.java, "main.db").build()
    }

    abstract fun questionDao(): QuestionDao

}