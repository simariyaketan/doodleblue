package com.doodleblue.test.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.doodleblue.test.model.ContactTableModel

@Database(entities = arrayOf(ContactTableModel::class), version = 1, exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao() : ContactDAOAccess

    companion object {

        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getDataseClient(context: Context) : ContactDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, ContactDatabase::class.java, "CONTACT_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }
        }

    }

}