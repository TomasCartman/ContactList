package com.blackpineapple.correiocontatos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ContactListViewModel : ViewModel() {
    private val repository = Repository.getInstance()
    lateinit var contactsLiveData: LiveData<List<Contact>>

    fun getAllContacts() {
        contactsLiveData = repository.getAllContacts()
    }

    fun putContact(name: String, phone: Long) {
        repository.putContact(name, phone)
    }

    fun deleteContact(name: String) {
        repository.deleteContact(name)
    }

    fun searchContact(searchName: String): List<Contact> {
        val listSearchName: MutableList<Contact> = mutableListOf()
        val contactList = contactsLiveData.value
        if(contactList != null) {
            for(contact in contactList) {
                if(contact.name.startsWith(searchName, true)) {
                    listSearchName.add(contact)
                }
            }
            return listSearchName
        }
        return emptyList()
    }
}