package com.blackpineapple.correiocontatos

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlin.IllegalStateException

class ContactFormFragment : DialogFragment() {
    interface ContactFormDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.dialog_contact, null))
                    .setPositiveButton(R.string.add_contact, DialogInterface.OnClickListener {
                        dialog, which -> targetFragment?.let {
                            fragment -> (fragment as ContactFormDialogListener).onDialogPositiveClick(this)
                        }
                    })
                    .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener {
                        dialog, which -> targetFragment?.let {
                            fragment -> (fragment as ContactFormDialogListener).onDialogNegativeClick(this)
                        }
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}