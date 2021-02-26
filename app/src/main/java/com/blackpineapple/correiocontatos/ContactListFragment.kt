package com.blackpineapple.correiocontatos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackpineapple.correiocontatos.recyclerview.ContactAdapter

private const val TAG = "ContactListFragment"

class ContactListFragment : Fragment() {
    private lateinit var contactListViewModel: ContactListViewModel
    private lateinit var contactRecyclerView: RecyclerView
    private lateinit var recyclerViewLinearLayoutManager: LinearLayoutManager
    private lateinit var progressBar: ProgressBar
    private var adapter: ContactAdapter = ContactAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)

        contactRecyclerView = view.findViewById(R.id.recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)

        recyclerViewLinearLayoutManager = LinearLayoutManager(context)
        contactRecyclerView.layoutManager = recyclerViewLinearLayoutManager
        contactRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}