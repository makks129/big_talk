package com.makks.bigtalk.model.remote

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.makks.bigtalk.model.domain.Question
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

interface QuestionRemoteDataSource {
    fun getQuestionsObservable(): Observable<Question>
    fun saveQuestionObservable(question: Question): Single<Question>
}

class QuestionRemoteDataSourceImpl @Inject constructor(
        private val firestore: FirebaseFirestore) : QuestionRemoteDataSource {

    override fun getQuestionsObservable(): Observable<Question> =
            Observable.fromCallable { Tasks.await(firestore.questions().get()) }
                    .flatMapIterable { it }
                    .map { it.toObject(Question::class.java).apply { id = it.id } }


    override fun saveQuestionObservable(question: Question): Single<Question> =
            Observable.fromCallable {
                Tasks.await(firestore.runTransaction {
                    val newQuestionDoc = firestore.questions().document()
                    it.set(newQuestionDoc, question)
                    val newQuestion = it.get(newQuestionDoc).toObject(Question::class.java)
                    newQuestion?.id = newQuestionDoc.id
                    newQuestion
                })
            }.singleOrError()

}