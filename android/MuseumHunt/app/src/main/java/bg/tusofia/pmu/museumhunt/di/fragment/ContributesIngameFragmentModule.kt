package bg.tusofia.pmu.museumhunt.di.fragment

import bg.tusofia.pmu.museumhunt.di.fragment.scope.FragmentScope
import bg.tusofia.pmu.museumhunt.ingame.browse.BrowseGamesFragment
import bg.tusofia.pmu.museumhunt.ingame.finish.GameFinishedFragment
import bg.tusofia.pmu.museumhunt.ingame.init.IngameHomeDestinationFragment
import bg.tusofia.pmu.museumhunt.ingame.location.MapFragment
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

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMapFragment(): MapFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeBrowseFragment(): BrowseGamesFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeGameFinishedFragment(): GameFinishedFragment
}