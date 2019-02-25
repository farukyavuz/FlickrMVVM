package org.faruk.flickrmvvm.di.component

import android.app.Application
import dagger.Component
import org.faruk.flickrmvvm.di.ViewModelModule
import org.faruk.flickrmvvm.di.module.AppModule
import org.faruk.flickrmvvm.ui.recent.RecentActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, ViewModelModule::class]
)
interface AppComponent {
    fun inject(app: Application)
    fun inject(recentActivity: RecentActivity)
}