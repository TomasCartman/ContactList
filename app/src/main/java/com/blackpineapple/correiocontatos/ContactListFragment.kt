package com.blackpineapple.correiocontatos

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.correiocontatos.recyclerview.ContactAdapter

private const val TAG = "ContactListFragment"

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

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        // Do nothing
        Toast.makeText(context, "onDialogNegativeClick", Toast.LENGTH_SHORT).show()
    }
}