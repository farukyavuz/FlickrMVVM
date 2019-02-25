package org.faruk.flickrmvvm.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.faruk.flickrmvvm.ui.recent.RecentViewModel


@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RecentViewModel::class)
    internal abstract fun recentViewModel(viewModel: RecentViewModel): ViewModel

    //Add more ViewModels here
}