package bg.tusofia.pmu.museumhunt.di.activity

import androidx.lifecycle.ViewModelProvider
import bg.tusofia.pmu.museumhunt.di.ActivityScopeViewModelFactory
import bg.tusofia.pmu.museumhunt.di.viewmodel.factory.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class BindActivityViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(@ActivityScopeViewModelFactory factory: ViewModelProviderFactory): ViewModelProvider.Factory

}