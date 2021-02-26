package com.blackpineapple.correiocontatos.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.correiocontatos.Contact
import com.blackpineapple.correiocontatos.R

class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var contact: Contact
    private val textViewContactName: TextView = view.findViewById(R.id.contact_name_text_view)
    private val textViewContactPhone: TextView = view.findViewById(R.id.contact_phone_text_view)

    fun bind(contact: Contact) {
        this.contact = contact

        textViewContactName.text = contact.name
        textViewContactPhone.text = contact.number.toString()
    }
}