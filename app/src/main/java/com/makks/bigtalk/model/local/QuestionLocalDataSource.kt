package com.makks.bigtalk.model.local

import com.makks.bigtalk.model.domain.Question
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import javax.inject.Inject

interface QuestionLocalDataSource {
    fun getQuestionsObservable(): Observable<Question>
    fun saveQuestions(questions: List<Question>)
    fun saveQuestion(question: Question)
}

class QuestionLocalDataSourceImpl @Inject constructor(
        private val questionDao: QuestionDao) : QuestionLocalDataSource {

    override fun getQuestionsObservable() = questionDao.getQuestions().toObservable()

    override fun saveQuestions(questions: List<Question>) = questionDao.saveQuestions(questions)

    override fun saveQuestion(question: Question) = questionDao.saveQuestion(question)

}