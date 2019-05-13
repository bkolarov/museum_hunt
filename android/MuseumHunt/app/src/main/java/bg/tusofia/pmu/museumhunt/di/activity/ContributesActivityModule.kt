package bg.tusofia.pmu.museumhunt.di.activity

import bg.tusofia.pmu.museumhunt.MainActivity
import bg.tusofia.pmu.museumhunt.di.activity.scope.ActivityScope
import bg.tusofia.pmu.museumhunt.di.fragment.ContributesIngameFragmentModule
import bg.tusofia.pmu.museumhunt.ingame.IngameActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ContributesActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class, ContributesIngameFragmentModule::class])
    abstract fun contributeInGameActivityInjector(): IngameActivity
}