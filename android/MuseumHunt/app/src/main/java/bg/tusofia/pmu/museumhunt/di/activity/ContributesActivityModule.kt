package bg.tusofia.pmu.museumhunt.di.activity

import bg.tusofia.pmu.museumhunt.MainActivity
import bg.tusofia.pmu.museumhunt.di.activity.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ContributesActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity

}