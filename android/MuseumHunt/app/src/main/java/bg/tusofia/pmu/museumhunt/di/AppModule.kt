package bg.tusofia.pmu.museumhunt.di

import android.content.Context
import bg.tusofia.pmu.museumhunt.application.MuseumHuntApplication
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManagerImpl
import bg.tusofia.pmu.museumhunt.di.location.LocationModule
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [LocationModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: MuseumHuntApplication): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideResourceManager(context: Context): ResourceManager = ResourceManagerImpl(context)
}