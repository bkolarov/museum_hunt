package bg.tusofia.pmu.museumhunt.di.domain.repository

import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.domain.db.dao.GameDao
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepositoryImpl
import bg.tusofia.pmu.museumhunt.domain.repository.LevelDataRepository
import bg.tusofia.pmu.museumhunt.domain.repository.LevelDataRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideGameRepository(gameDao: GameDao): GameRepository = GameRepositoryImpl(gameDao)

    @Provides
    @Singleton
    fun provideLevelDataRepository(resourceManager: ResourceManager): LevelDataRepository = LevelDataRepositoryImpl(resourceManager)

}