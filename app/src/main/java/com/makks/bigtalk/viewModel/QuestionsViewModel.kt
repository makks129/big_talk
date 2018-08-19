package com.makks.bigtalk.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.makks.bigtalk.global.App
import com.makks.bigtalk.global.extensions.EventResource
import com.makks.bigtalk.global.extensions.Resource
import com.makks.bigtalk.global.extensions.removeAndAddSource
import com.makks.bigtalk.model.domain.Question
import com.makks.bigtalk.model.repository.QuestionRepository
import javax.inject.Inject

class QuestionsViewModel : ViewModel() {
    init {
        App.appComponent.inject(this)
    }

    @Inject
    lateinit var repository: QuestionRepository
    val getQuestionsData = MediatorLiveData<Resource<List<Question>>>()
    val saveQuestionData = MediatorLiveData<EventResource<Question>>()
    var lastNewQuestion: Question? = null
        private set
    var selectedQuestionIndex = 0

    fun loadQuestions(refresh: Boolean = false) {
        val data = repository.getQuestions(refresh)
        getQuestionsData.removeAndAddSource(data, getQuestionsData::setValue)
    }

    fun saveQuestion(question: Question) {
        lastNewQuestion = question
        val data = repository.saveQuestion(question)
        saveQuestionData.removeAndAddSource(data, saveQuestionData::setValue)
    }

    fun saveLastNewQuestion() {
        lastNewQuestion?.let { saveQuestion(it) }
    }

    override fun onCleared() {
        repository.destroy()
    }

}