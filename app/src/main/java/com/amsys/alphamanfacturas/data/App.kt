package com.amsys.alphamanfacturas.data

import com.amsys.alphamanfacturas.data.components.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

//    override fun onCreate() {
//        super.onCreate()
//        configureWorkManager()
//    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
    }
//
//    @Inject
//    lateinit var daggerAwareWorkerFactory: WorkManagerFactory
//
//    private fun configureWorkManager() {
//        val config = Configuration.Builder()
//            .setWorkerFactory(daggerAwareWorkerFactory)
//            .build()
//        WorkManager.initialize(this, config)
//    }
}