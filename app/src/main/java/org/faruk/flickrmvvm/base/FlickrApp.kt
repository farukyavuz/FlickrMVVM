package org.faruk.flickrmvvm.base

import android.app.Application
import org.faruk.flickrmvvm.di.component.AppComponent
import org.faruk.flickrmvvm.di.component.DaggerAppComponent
import org.faruk.flickrmvvm.di.module.AppModule

class FlickrApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}