package com.makks.bigtalk.global

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.makks.bigtalk.model.local.LocalModule
import com.makks.bigtalk.model.remote.RemoteModule
import com.makks.bigtalk.model.repository.RepositoryModule

class App : MultiDexApplication() {

    companion object {
        lateinit var instance: App
            private set
        lateinit var appComponent: AppComponent
            private set

        fun context(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
                .repositoryModule(RepositoryModule())
                .remoteModule(RemoteModule())
                .localModule(LocalModule())
                .build()
    }

}