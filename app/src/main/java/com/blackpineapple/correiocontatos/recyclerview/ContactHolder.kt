package com.blackpineapple.correiocontatos.recyclerview

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.correiocontatos.Contact
import com.blackpineapple.correiocontatos.R

class ContactHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
    private lateinit var contact: Contact
    private val textViewContactName: TextView = view.findViewById(R.id.contact_name_text_view)
    private val textViewContactPhone: TextView = view.findViewById(R.id.contact_phone_text_view)

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    fun bind(contact: Contact) {
        this.contact = contact

        textViewContactName.text = contact.name
        textViewContactPhone.text = contact.number.toString()
    }

    override fun onClick(v: View?) {
        val intent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:" + contact.number.toString())
        }
        v?.context?.startActivity(intent)
    }

    override fun onLongClick(v: View?): Boolean {
        Toast.makeText(itemView.context, "onLongClick", Toast.LENGTH_SHORT).show()
        return true
    }
}