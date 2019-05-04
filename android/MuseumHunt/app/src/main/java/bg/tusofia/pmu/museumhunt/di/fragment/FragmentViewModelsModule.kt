package bg.tusofia.pmu.museumhunt.di.fragment

import androidx.lifecycle.ViewModel
import bg.tusofia.pmu.museumhunt.di.viewmodel.annotation.ViewModelKey
import bg.tusofia.pmu.museumhunt.ingame.init.IngameHomeDestinationViewModel
import bg.tusofia.pmu.museumhunt.ingame.riddle.RiddleViewModel
import bg.tusofia.pmu.museumhunt.ingame.unity.UnityLauncherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(RiddleViewModel::class)
    abstract fun bindRiddleViewModel(viewModel: RiddleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IngameHomeDestinationViewModel::class)
    abstract fun bindIngameHomeDestinationViewModel(viewModel: IngameHomeDestinationViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(UnityLauncherViewModel::class)
    abstract fun bindUnityLauncherViewModel(viewModel: UnityLauncherViewModel): ViewModel
}