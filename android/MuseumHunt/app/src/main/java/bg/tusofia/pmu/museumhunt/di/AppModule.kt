package bg.tusofia.pmu.museumhunt.di

import android.content.Context
import bg.tusofia.pmu.museumhunt.application.MuseumHuntApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: MuseumHuntApplication): Context {
        return application
    }


}