package com.makks.bigtalk.global

import com.makks.bigtalk.model.local.LocalModule
import com.makks.bigtalk.model.remote.RemoteModule
import com.makks.bigtalk.model.repository.RepositoryModule
import com.makks.bigtalk.viewModel.QuestionsViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    RepositoryModule::class,
    RemoteModule::class,
    LocalModule::class
])
@Singleton
interface AppComponent {

    fun inject(questionsViewModel: QuestionsViewModel)

}