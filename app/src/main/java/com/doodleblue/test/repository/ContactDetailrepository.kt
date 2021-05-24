package com.doodleblue.test.repository

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import com.doodleblue.test.model.ContactDetail
import com.doodleblue.test.model.ContactTableModel
import com.doodleblue.test.room.ContactDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ContactRepository(private val context: Context) {
    private val contacts: MutableList<ContactDetail>

    var contactDatabase: ContactDatabase? = null
    var contactTableList: LiveData<List<ContactTableModel>>? = null

    fun fetchContacts(): List<ContactDetail> {

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor?.count ?: 0 > 0) {
            while (cursor!!.moveToNext()) {
                val contactDetail = ContactDetail()
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNo =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val photoUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                Log.e("contact", "getAllContacts: $name $phoneNo $photoUri")
                if (name != null && phoneNo != null && name != "" && phoneNo != "") {
                    contactDetail.name = name
                    contactDetail.phoneNumber = phoneNo
                    contactDetail.photoUri = photoUri
                    contacts.add(contactDetail)
                }
            }
        }
        cursor?.close()
        return contacts
    }

    init {
        contacts = ArrayList<ContactDetail>()
    }

    fun initializeDB(context: Context): ContactDatabase {
        return ContactDatabase.getDataseClient(context)
    }

    fun insertData(context: Context, name: String, number: String) {

        contactDatabase = initializeDB(context)

        CoroutineScope(IO).launch {
            val contactTableModel = ContactTableModel(name, number)
            contactDatabase!!.contactDao().InsertData(contactTableModel)
        }

    }

    fun removeData(context: Context, name: String, number: String) {
        contactDatabase = initializeDB(context)
        CoroutineScope(IO).launch {
            contactDatabase!!.contactDao().removeContactDetails(name)
        }
    }

    fun getContactList(context: Context): LiveData<List<ContactTableModel>>? {
        contactDatabase = initializeDB(context)
        contactTableList = contactDatabase!!.contactDao().getContactDetails()
        return contactTableList
    }

    fun getFavoriteContact(context: Context, number : String): ContactTableModel? {
        contactDatabase = initializeDB(context)
        var contactTableModel : ContactTableModel = contactDatabase!!.contactDao().getFavoriteContactDetails(number)
        return contactTableModel
    }
}