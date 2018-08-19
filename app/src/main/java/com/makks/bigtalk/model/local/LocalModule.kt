package com.makks.bigtalk.model.local

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

    @Provides
    @Singleton
    fun provideAppRoomDatabase() = AppRoomDatabase.build()

    @Provides
    @Singleton
    fun provideQuestionDao(db: AppRoomDatabase) = db.questionDao()

    @Provides
    @Singleton
    fun provideQuestionLocalDataSource(dao: QuestionDao): QuestionLocalDataSource =
            QuestionLocalDataSourceImpl(dao)

}