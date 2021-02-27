package com.blackpineapple.correiocontatos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlin.IllegalStateException

class ContactFormFragment : DialogFragment() {
    interface ContactFormDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, name: String, number: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.dialog_contact, null)

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
                    .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener {
                        _, _ -> targetFragment?.let {
                            fragment -> (fragment as ContactFormDialogListener).onDialogNegativeClick(this)
                        }
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}