package com.doodleblue.test.viewmodel

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import com.doodleblue.test.model.ContactDetail
import com.doodleblue.test.model.ContactTableModel
import com.doodleblue.test.repository.ContactRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactViewModel(context: Context) : BaseObservable() {

    private val contacts: ObservableArrayList<ContactDetail>
    private val repository: ContactRepository

    var liveDataContact: LiveData<List<ContactTableModel>>? = null

    fun getContacts(): List<ContactDetail> {
        contacts.addAll(repository.fetchContacts())
        return contacts
    }

    fun insertData(context: Context, name: String, number: String) {
        repository.insertData(context, name, number)
    }

    fun removeFavoriteContact(context: Context, name: String, number: String) {
        repository.removeData(context, name, number)
    }

    fun getFavoriteContact(context: Context): LiveData<List<ContactTableModel>>? {

        liveDataContact = repository.getContactList(context)
        return liveDataContact
    }

    init {
        contacts = ObservableArrayList()
        repository = ContactRepository(context)
    }
}