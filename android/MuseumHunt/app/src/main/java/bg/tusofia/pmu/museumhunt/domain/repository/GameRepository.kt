package bg.tusofia.pmu.museumhunt.domain.repository

import bg.tusofia.pmu.museumhunt.domain.db.dao.GameDao
import bg.tusofia.pmu.museumhunt.domain.db.dao.LevelProgressDao
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelProgress
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface GameRepository {
    fun getGames(): Single<List<Game>>

    fun addGame(game: Game): Single<Long>

    fun getGameById(gameId: Long): Single<Game?>

    fun updateLevelProgress(levelProgress: LevelProgress): Completable

    fun setLevelStage(levelId: Long, levelStage: LevelStage): Completable

    fun addLevelProgress(levelProgress: LevelProgress): Single<Long>

    fun getLevelProgressById(levelId: Long): Single<LevelProgress>
}

class GameRepositoryImpl(
    private val gameDao: GameDao,
    private val levelProgressDao: LevelProgressDao
) : GameRepository {

    override fun getGames(): Single<List<Game>> {
        return gameDao.getAllGames()
            .subscribeOn(Schedulers.io())
    }

    override fun addGame(game: Game) = gameDao.insertNewGame(game).subscribeOn(Schedulers.io())

    override fun getGameById(gameId: Long): Single<Game?> = gameDao.getGameById(gameId).subscribeOn(Schedulers.io())

    override fun updateLevelProgress(levelProgress: LevelProgress) =
        levelProgressDao.updateLevelProgress(levelProgress).subscribeOn(Schedulers.io())

    override fun setLevelStage(levelId: Long, levelStage: LevelStage) =
        levelProgressDao.updateLevelStage(levelId, levelStage).subscribeOn(Schedulers.io())

    override fun addLevelProgress(levelProgress: LevelProgress): Single<Long> =
        levelProgressDao.insertLevelProgress(levelProgress).subscribeOn(Schedulers.io())

    override fun getLevelProgressById(levelId: Long): Single<LevelProgress> =
        levelProgressDao.getLevelProgressById(levelId).subscribeOn(Schedulers.io())
}