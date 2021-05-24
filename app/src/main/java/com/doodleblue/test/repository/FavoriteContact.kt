package com.doodleblue.test.repository

import android.content.Context
import android.os.AsyncTask
import com.doodleblue.test.model.ContactTableModel
import com.doodleblue.test.room.ContactDatabase


class FavoriteContact() {

    fun getAllUsers(context: Context, incomingNumber : String): ContactTableModel? {
        return GetUsersAsyncTask(context,incomingNumber).execute().get()
    }


    class GetUsersAsyncTask(var context : Context, var incomingNumber : String) :
        AsyncTask<Void?, Void?, ContactTableModel>() {

        override fun doInBackground(vararg params: Void?): ContactTableModel {
           return ContactDatabase.getDataseClient(context!!).contactDao()
                .getFavoriteContactDetails(incomingNumber)
        }
    }
}