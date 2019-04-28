package bg.tusofia.pmu.museumhunt.di

import bg.tusofia.pmu.museumhunt.application.MuseumHuntApplication
import bg.tusofia.pmu.museumhunt.di.activity.ContributesActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ContributesActivityModule::class])
interface AppComponent : AndroidInjector<MuseumHuntApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MuseumHuntApplication): Builder

        fun build(): AppComponent
    }

}
