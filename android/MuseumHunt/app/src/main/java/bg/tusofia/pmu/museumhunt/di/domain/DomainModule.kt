package bg.tusofia.pmu.museumhunt.di.domain

import bg.tusofia.pmu.museumhunt.di.domain.db.DBModule
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DBModule::class])
class DomainModule {
    @Provides
    @Singleton
    fun provideGson() = Gson()

}