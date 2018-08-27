package com.makks.bigtalk.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makks.bigtalk.global.extensions.EmptyLiveData
import com.makks.bigtalk.global.extensions.EventResource
import com.makks.bigtalk.global.extensions.Resource
import com.makks.bigtalk.global.extensions.isEmpty
import com.makks.bigtalk.model.domain.Question
import com.makks.bigtalk.model.local.QuestionLocalDataSource
import com.makks.bigtalk.model.remote.QuestionRemoteDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface QuestionRepository {
    fun getQuestions(refresh: Boolean = false): LiveData<Resource<List<Question>>>
    fun saveQuestion(question: Question): LiveData<EventResource<Question>>
    fun destroy()
}

class QuestionRepositoryImpl @Inject constructor(
        private val local: QuestionLocalDataSource,
        private val remote: QuestionRemoteDataSource) : QuestionRepository {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var questionsCache: MutableLiveData<Resource<List<Question>>> = EmptyLiveData()
    private var firstTime = true

    override fun getQuestions(refresh: Boolean): LiveData<Resource<List<Question>>> {
        if (firstTime || refresh) {
            firstTime = false
            getQuestionsFromRemote()
        }
        if (questionsCache.isEmpty()) {
            getQuestionsFromLocal()
        }
        return questionsCache
    }

    private fun getQuestionsFromRemote() {
        questionsCache = MutableLiveData()
        questionsCache.value = Resource.Loading()
        val disposable = remote.getQuestionsObservable().toList()
                .doOnSuccess { it.shuffle() }
                .doOnSuccess { local.saveQuestions(it) }
                .onErrorResumeNext { local.getQuestionsObservable().toList() }
                .doOnSuccess { if (it.isEmpty()) throw RuntimeException() }
                .subscribeGetQuestions()
        compositeDisposable.add(disposable)
    }

    private fun getQuestionsFromLocal() {
        questionsCache = MutableLiveData()
        questionsCache.value = Resource.Loading()
        val disposable = local.getQuestionsObservable().toList()
                .doOnSuccess { if (it.isEmpty()) throw RuntimeException() }
                .subscribeGetQuestions()
        compositeDisposable.add(disposable)
    }

    private fun Single<List<Question>>.subscribeGetQuestions(): Disposable =
            subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ questionsCache.value = Resource.Success(it) },
                            { questionsCache.value = Resource.Error(it) })

    override fun saveQuestion(question: Question): LiveData<EventResource<Question>> {
        val responseData = MutableLiveData<EventResource<Question>>()
        responseData.value = EventResource.Loading()
        val disposable = remote.saveQuestionObservable(question)
                .doOnSuccess { local.saveQuestion(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    if (questionsCache.value is Resource.Success) {
                        val value = questionsCache.value as Resource.Success
                        questionsCache.value = Resource.Success(value.data + it)
                    }
                }
                .subscribe({ responseData.value = EventResource.Success(it) },
                        { responseData.value = EventResource.Error(it) })
        compositeDisposable.add(disposable)
        return responseData
    }

    override fun destroy() {
        compositeDisposable.dispose()
    }

}