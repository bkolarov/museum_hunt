package bg.tusofia.pmu.museumhunt.di.activity

import androidx.lifecycle.ViewModel
import bg.tusofia.pmu.museumhunt.di.activity.scope.ActivityScope
import bg.tusofia.pmu.museumhunt.di.viewmodel.factory.ViewModelFactoryModule
import bg.tusofia.pmu.museumhunt.di.viewmodel.factory.ViewModelProviderFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module(
    includes = [
        ActivityViewModelModule::class,
        ViewModelFactoryModule::class
    ]
)
class ActivityModule {

    @Provides
    @ActivityScope
    fun provideViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ) = ViewModelProviderFactory(creators)

}