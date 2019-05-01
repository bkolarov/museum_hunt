package bg.tusofia.pmu.museumhunt.domain.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bg.tusofia.pmu.museumhunt.domain.db.TableConstants
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GameDao {

    @Query("SELECT * FROM " + TableConstants.GAMES)
    fun getAllGames(): Flowable<List<Game>>

    @Query("SELECT * FROM " + TableConstants.GAMES + " WHERE id = :gameId")
    fun getGameById(gameId: Int): Single<Game?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewGame(game: Game): Completable

}