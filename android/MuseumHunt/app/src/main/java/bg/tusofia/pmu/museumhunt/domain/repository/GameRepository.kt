package bg.tusofia.pmu.museumhunt.domain.repository

import bg.tusofia.pmu.museumhunt.domain.db.dao.GameDao
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface GameRepository {
    fun getGames(): Single<List<Game>>

    fun addGame(game: Game): Completable

    fun getGameById(gameId: Int): Single<Game?>
}

class GameRepositoryImpl(private val gameDao: GameDao) : GameRepository {

    override fun getGames(): Single<List<Game>> {
        return gameDao.getAllGames()
            .subscribeOn(Schedulers.io())
            .first(listOf())
    }

    override fun addGame(game: Game) = gameDao.insertNewGame(game).subscribeOn(Schedulers.io())

    override fun getGameById(gameId: Int): Single<Game?> = gameDao.getGameById(gameId).subscribeOn(Schedulers.io())
}