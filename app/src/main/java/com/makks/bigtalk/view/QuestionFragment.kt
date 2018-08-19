package com.makks.bigtalk.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.makks.bigtalk.R
import com.makks.bigtalk.global.extensions.Resource
import com.makks.bigtalk.global.extensions.putArgs
import com.makks.bigtalk.viewModel.QuestionsViewModel

class QuestionFragment : Fragment() {

    companion object {
        private const val ARG_INDEX = "index"

        fun newInstance(index: Int): QuestionFragment =
                QuestionFragment().putArgs {
                    putInt(ARG_INDEX, index)
                }
    }

    private lateinit var viewModel: QuestionsViewModel
    private var index: Int = 0

    private lateinit var vText: TextView
    private lateinit var vNumber: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = arguments!!.getInt(ARG_INDEX)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.question_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vText = view.findViewById(R.id.text)
        vNumber = view.findViewById(R.id.number)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(parentFragment!!).get(QuestionsViewModel::class.java)
        viewModel.getQuestionsData.observe(this, Observer {
            if (it is Resource.Success) {
                val (_, number, text) = it.data[index]
                vText.text = text
                vNumber.text = "$number."
            }
        })
    }

}