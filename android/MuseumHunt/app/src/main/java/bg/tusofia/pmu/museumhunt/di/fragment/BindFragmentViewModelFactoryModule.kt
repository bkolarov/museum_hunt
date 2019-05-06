package bg.tusofia.pmu.museumhunt.di.fragment

import androidx.lifecycle.ViewModelProvider
import bg.tusofia.pmu.museumhunt.di.FragmentScopeViewModelFactory
import bg.tusofia.pmu.museumhunt.di.viewmodel.factory.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class BindFragmentViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(@FragmentScopeViewModelFactory factory: ViewModelProviderFactory): ViewModelProvider.Factory

}