package com.blackpineapple.correiocontatos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ContactListViewModel : ViewModel() {
    private val repository = Repository.getInstance()
    lateinit var contactsLiveData: LiveData<List<Contact>>

    fun getAllContacts() {
        contactsLiveData = repository.getAllContacts()
    }
}