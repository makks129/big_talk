package com.makks.bigtalk.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.DialogFragment
import com.makks.bigtalk.R
import com.makks.bigtalk.global.extensions.putArgs
import com.makks.bigtalk.global.extensions.showKeyboard
import com.makks.bigtalk.model.domain.Question

class AddQuestionDialogFragment: DialogFragment() {

    companion object {
        val TAG = "TAG_" + AddQuestionDialogFragment::class.java.simpleName
        private const val ARG_QUESTION = "question"

        fun newInstance(question: Question?): AddQuestionDialogFragment =
                AddQuestionDialogFragment().putArgs {
                    putParcelable(ARG_QUESTION, question)
                }
    }

    lateinit var addQuestion: (Question) -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val themedContext = ContextThemeWrapper(activity!!, R.style.AlertDialogTheme)

        val view = View.inflate(themedContext, R.layout.dialog_add_question, null)
        val input = view.findViewById<EditText>(R.id.input)

        val question = arguments?.getParcelable<Question>(ARG_QUESTION)
        question?.let { input.setText(it.text) }
        input.setSelection(input.text.length)

        return AlertDialog.Builder(themedContext)
                .setTitle(R.string.add_new_question)
                .setView(view)
                .setPositiveButton(R.string.add) { _, _ ->
                    if (input.text.isBlank()) {
                        Toast.makeText(activity, R.string.no_question_to_save, LENGTH_SHORT).show()
                    } else {
                        addQuestion(Question(text = input.text.toString(), custom = true))
                    }
                }
                .setNegativeButton(R.string.cancel, null)
                .create().apply { showKeyboard() }
    }

}