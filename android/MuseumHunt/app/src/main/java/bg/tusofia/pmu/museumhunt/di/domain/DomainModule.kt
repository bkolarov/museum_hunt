package bg.tusofia.pmu.museumhunt.di.domain

import bg.tusofia.pmu.museumhunt.di.domain.db.DBModule
import bg.tusofia.pmu.museumhunt.di.domain.repository.RepositoryModule
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DBModule::class, RepositoryModule::class])
class DomainModule {

    @Provides
    @Singleton
    fun provideGson() = Gson()

}