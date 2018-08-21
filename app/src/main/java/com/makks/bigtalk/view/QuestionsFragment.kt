package com.makks.bigtalk.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.makks.bigtalk.R
import com.makks.bigtalk.global.extensions.EventResource
import com.makks.bigtalk.global.extensions.EventResourceObserver
import com.makks.bigtalk.global.extensions.Resource
import com.makks.bigtalk.global.extensions.TriStateViewGroups
import com.makks.bigtalk.view.dialog.AddQuestionDialogFragment
import com.makks.bigtalk.viewModel.QuestionsViewModel
import kotlinx.android.synthetic.main.abc_screen_toolbar.*

class QuestionsFragment : Fragment() {

    companion object {
        fun newInstance() = QuestionsFragment()
    }

    private lateinit var viewModel: QuestionsViewModel

    private lateinit var groups: TriStateViewGroups
    private lateinit var viewPager: ViewPager
    private val adapter by lazy { Adapter(childFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.questions_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groups = TriStateViewGroups(view, R.id.groupContent, R.id.groupLoading, R.id.groupError)
        viewPager = view.findViewById(R.id.viewPager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                viewModel.selectedQuestionIndex = position
            }
        })
        view.findViewById<View>(R.id.retry).setOnClickListener {
            viewModel.loadQuestions(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        viewModel = ViewModelProviders.of(this).get(QuestionsViewModel::class.java)

        viewModel.getQuestionsData.observe(this, Observer {
            when (it) {
                is Resource.Loading -> groups.showLoading()
                is Resource.Success -> {
                    groups.showContent()
                    adapter.size = it.data.size
                    adapter.notifyDataSetChanged()
                    viewPager.currentItem = viewModel.selectedQuestionIndex
                }
                is Resource.Error -> groups.showError()
            }
        })
        viewModel.loadQuestions()

        viewModel.saveQuestionData.observe(this, EventResourceObserver {
            when (it) {
                is EventResource.Loading ->
                    Toast.makeText(activity, R.string.saving_new_question, LENGTH_SHORT).show()
                is EventResource.Success ->
                    Toast.makeText(activity, R.string.new_question_saved, LENGTH_SHORT).show()
                is EventResource.Error ->
                    showSaveQuestionError()
            }
        })
    }

    private fun showSaveQuestionError() {
        Snackbar.make(view!!, R.string.error_saving_question, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry) { viewModel.saveLastNewQuestion() }
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.questions, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add -> showAddQuestionDialog()
            R.id.refresh -> viewModel.loadQuestions(true)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddQuestionDialog() {
        AddQuestionDialogFragment.newInstance(viewModel.lastNewQuestion)
                .show(childFragmentManager, AddQuestionDialogFragment.TAG)
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        (childFragment as? AddQuestionDialogFragment)?.addQuestion = viewModel::saveQuestion
    }

    private class Adapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        var size = 0

        override fun getItem(position: Int): Fragment {
            return QuestionFragment.newInstance(position)
        }

        override fun getCount() = size
    }

}