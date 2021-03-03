package com.blackpineapple.correiocontatos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "ContactListFragment"
const val DIALOG_CONTACT_FORM = "DialogContactForm"

class ContactListFragment : Fragment(), ContactFormFragment.ContactFormDialogListener {
    private lateinit var contactListViewModel: ContactListViewModel
    private lateinit var contactRecyclerView: RecyclerView
    private lateinit var recyclerViewLinearLayoutManager: LinearLayoutManager
    private lateinit var progressBar: ProgressBar
    private var adapter: ContactAdapter = ContactAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        val factory = ViewModelProvider.NewInstanceFactory()
        val viewModelProvider = ViewModelProvider(viewModelStore, factory)
        contactListViewModel = viewModelProvider.get(ContactListViewModel::class.java)

        contactListViewModel.getAllContacts()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_list_menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null) {
                        // Search in the contact list
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText.isNullOrEmpty()) {
                        // Put in the adapter the entire list of contacts cause the newText is null or empty
                    }
                    return true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_item_add -> {
                // open table to add a new contact
                val newFragment = ContactFormFragment()
                newFragment.setTargetFragment(this, 1)
                newFragment.show(parentFragmentManager, "contact_form")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)

        contactRecyclerView = view.findViewById(R.id.recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)

        recyclerViewLinearLayoutManager = LinearLayoutManager(context)
        contactRecyclerView.layoutManager = recyclerViewLinearLayoutManager
        contactRecyclerView.adapter = adapter
        contactRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactListViewModel.contactsLiveData.observe(
            viewLifecycleOwner,
            Observer {
                contactList -> adapter.submitList(contactList)
                contactRecyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        )
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, name: String, number: String) {
        // Save or update the contact
        Toast.makeText(context, "onDialogPositiveClick", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "\nName: $name\nNumber: $number")
        if(name.isNotEmpty() && number.isNotEmpty()) {
            val phone = number.toLong()
            contactListViewModel.putContact(name, phone)
        } else {
            Toast.makeText(context, "Name and number length should be greater then 1 character", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment, name: String) {
        // Delete the contact
        Toast.makeText(context, "onDialogNegativeClick", Toast.LENGTH_SHORT).show()
        if(name.isNotEmpty()) contactListViewModel.deleteContact(name)
        else Toast.makeText(context, "You cannot delete a contact before put it on the list", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        // Do nothing
        Toast.makeText(context, "onDialogNeutralClick", Toast.LENGTH_SHORT).show()
    }

    private inner class ContactHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
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
            ContactFormFragment.newInstance(contact.name, contact.number).apply {
                setTargetFragment(this@ContactListFragment, 1)
                show(this@ContactListFragment.parentFragmentManager , DIALOG_CONTACT_FORM)
            }
            return true
        }
    }

    private inner class ContactAdapter : androidx.recyclerview.widget.ListAdapter<Contact, ContactHolder>(ContactDiffCallback()) {
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
}