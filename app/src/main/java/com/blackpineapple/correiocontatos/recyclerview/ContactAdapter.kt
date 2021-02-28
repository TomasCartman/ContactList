package com.blackpineapple.correiocontatos.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import com.blackpineapple.correiocontatos.Contact
import com.blackpineapple.correiocontatos.R

class ContactAdapter : androidx.recyclerview.widget.ListAdapter<Contact, ContactHolder>(ContactDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact, parent, false)

        return ContactHolder(view)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val contact = getItem(position)

        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_animation)
        holder.itemView.startAnimation(animation)

        holder.bind(contact)
    }
}

class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}