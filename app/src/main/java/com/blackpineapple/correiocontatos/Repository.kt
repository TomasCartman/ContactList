package com.blackpineapple.correiocontatos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val TAG = "Repository"
private const val CHILD_CONTACT = "contacts"

class Repository {
    private var database: DatabaseReference
    private var databaseNotReference = Firebase.database
    private val girlLiveData : MutableLiveData<List<Contact>> = MutableLiveData()

    init {
        databaseNotReference.setPersistenceEnabled(true)
        database = databaseNotReference.reference
        database.keepSynced(true)
        database.addChildEventListener(ContactEventListener())
    }

    fun getAllContacts(): LiveData<List<Contact>> {
        database.child(CHILD_CONTACT).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contactList = mutableListOf<Contact>()
                    for (contact in snapshot.children) {
                        val name = contact.key
                        val phone = contact.child("number").value
                        val contactItem = name?.let { Contact(it, phone as Long) }

                        if (contactItem != null) {
                            contactList.add(contactItem)
                        }
                    }

                    Log.i(TAG, "Size: ${snapshot.childrenCount}")
                    Log.i(TAG, "onChildAdd")
                    girlLiveData.value = contactList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "Error: $error")
                }
            }
        )

        return girlLiveData
    }

    fun putContact(name: String, phone: Long) {
        val hashMap = HashMap<String, Long>()
        hashMap["number"] = phone
        database.child(CHILD_CONTACT).child(name).setValue(hashMap)
    }

    fun deleteContact(name: String) {
        database.child(CHILD_CONTACT).child(name).setValue(null)
    }

    private inner class ContactEventListener : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val contactList = mutableListOf<Contact>()
            for (contact in snapshot.children) {
                val name = contact.key
                val phone = contact.child("number").value
                val contactItem = name?.let { Contact(it, phone as Long) }

                if (contactItem != null) {
                    contactList.add(contactItem)
                }
            }

            Log.i(TAG, "Size: ${snapshot.childrenCount}")
            Log.i(TAG, "onChildAdd")
            girlLiveData.value = contactList
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val contactList = mutableListOf<Contact>()
            for (contact in snapshot.children) {
                val name = contact.key
                val phone = contact.child("number").value
                val contactItem = name?.let { Contact(it, phone as Long) }

                if (contactItem != null) {
                    contactList.add(contactItem)
                }
            }

            Log.i(TAG, "Size: ${snapshot.childrenCount}")
            Log.i(TAG, "onChildAdd")
            girlLiveData.value = contactList
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val contactList = mutableListOf<Contact>()
            for (contact in snapshot.children) {
                val name = contact.key
                val phone = contact.child("number").value
                val contactItem = name?.let { Contact(it, phone as Long) }

                if (contactItem != null) {
                    contactList.add(contactItem)
                }
            }

            Log.i(TAG, "Size: ${snapshot.childrenCount}")
            Log.i(TAG, "onChildAdd")
            girlLiveData.value = contactList
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onCancelled(error: DatabaseError) {
            Log.i(TAG, "onCancelled: $error")
        }

    }

    companion object {
        private var INSTANCE: Repository? = null

        fun getInstance(): Repository {
            if(INSTANCE == null) {
                INSTANCE = Repository()
            }
            return INSTANCE as Repository
        }
    }
}