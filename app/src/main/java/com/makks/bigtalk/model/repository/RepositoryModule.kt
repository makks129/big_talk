package com.makks.bigtalk.model.repository

import com.makks.bigtalk.model.local.QuestionLocalDataSource
import com.makks.bigtalk.model.remote.QuestionRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    fun provideQuestionRepository(local: QuestionLocalDataSource,
                                  remote: QuestionRemoteDataSource): QuestionRepository =
            QuestionRepositoryImpl(local, remote)

}