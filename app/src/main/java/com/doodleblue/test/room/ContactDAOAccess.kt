package com.doodleblue.test.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.doodleblue.test.model.ContactTableModel

@Dao
interface ContactDAOAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertData(contactTableModel: ContactTableModel)

    @Query("SELECT * FROM favoriteContact")
    fun getContactDetails() : LiveData<List<ContactTableModel>>

    @Query("DELETE FROM favoriteContact WHERE name =:name")
    fun removeContactDetails(name: String?)

    @Query("SELECT * FROM favoriteContact WHERE number =:number")
    fun getFavoriteContactDetails(number: String?) : ContactTableModel
}