package bg.tusofia.pmu.museumhunt.di.activity

import androidx.lifecycle.ViewModel
import bg.tusofia.pmu.museumhunt.MainActivityViewModel
import bg.tusofia.pmu.museumhunt.di.viewmodel.annotation.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

}