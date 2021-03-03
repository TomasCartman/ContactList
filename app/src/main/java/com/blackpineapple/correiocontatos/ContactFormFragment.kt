package com.blackpineapple.correiocontatos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlin.IllegalStateException

const val ARG_NAME = "name"
const val ARG_NUMBER = "number"

class ContactFormFragment : DialogFragment() {
    interface ContactFormDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, name: String, number: String)
        fun onDialogNegativeClick(dialog: DialogFragment, name: String)
        fun onDialogNeutralClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.dialog_contact, null)

            if(arguments != null) {
                val nameEditText = view.findViewById<EditText>(R.id.name_editText)
                val numberEditText = view.findViewById<EditText>(R.id.phone_editText)
                val name = arguments!!.get(ARG_NAME) as String
                val number = arguments!!.get(ARG_NUMBER) as Long
                nameEditText.apply {
                    setText(name)
                    isEnabled = false
                }
                numberEditText.setText(number.toString())
            }

            builder.setTitle(R.string.contact)
            builder.setView(view)
                    .setPositiveButton(R.string.save, DialogInterface.OnClickListener {
                        _, _ ->
                        val nameEditText = view.findViewById<EditText>(R.id.name_editText)
                        val numberEditText = view.findViewById<EditText>(R.id.phone_editText)
                        val name = nameEditText.text.toString()
                        val number = numberEditText.text.toString()
                        targetFragment?.let {
                            fragment -> (fragment as ContactFormDialogListener).onDialogPositiveClick(this, name, number)
                        }
                    })
                    .setNeutralButton(R.string.cancel, DialogInterface.OnClickListener {
                        _, _ ->
                        targetFragment?.let {
                            fragment -> (fragment as ContactFormDialogListener).onDialogNeutralClick(this)
                        }
                    })
                    .setNegativeButton(R.string.delete, DialogInterface.OnClickListener {
                        _, _ ->
                        val nameEditText = view.findViewById<EditText>(R.id.name_editText)
                        val name = nameEditText.text.toString()
                        targetFragment?.let {
                            fragment -> (fragment as ContactFormDialogListener).onDialogNegativeClick(this, name)
                        }
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        fun newInstance(name: String, number: Long): ContactFormFragment {
            val args = Bundle().apply {
                putSerializable(ARG_NAME, name)
                putSerializable(ARG_NUMBER, number)
            }
            return ContactFormFragment().apply { arguments = args }
        }
    }
}