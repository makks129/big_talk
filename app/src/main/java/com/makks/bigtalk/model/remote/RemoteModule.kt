package com.makks.bigtalk.model.remote

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RemoteModule {

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideQuestionRemoteDataSource(firestore: FirebaseFirestore): QuestionRemoteDataSource =
            QuestionRemoteDataSourceImpl(firestore)


}