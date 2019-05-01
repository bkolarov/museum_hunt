package bg.tusofia.pmu.museumhunt.di.fragment

import bg.tusofia.pmu.museumhunt.di.fragment.scope.FragmentScope
import bg.tusofia.pmu.museumhunt.ingame.init.IngameHomeDestinationFragment
import bg.tusofia.pmu.museumhunt.ingame.riddle.RiddleFragment
import bg.tusofia.pmu.museumhunt.ingame.unity.UnityLauncherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ContributesIngameFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeIngameHomeDestinationFragment(): IngameHomeDestinationFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeRiddleFragment(): RiddleFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeUnityLauncherFragment(): UnityLauncherFragment
}