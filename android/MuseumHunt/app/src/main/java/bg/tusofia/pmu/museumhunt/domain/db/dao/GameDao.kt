package bg.tusofia.pmu.museumhunt.domain.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import io.reactivex.Single

@Dao
interface GameDao {

    @Query("SELECT * FROM Games")
    fun getAllGames(): Single<List<Game>>

    @Query("SELECT * FROM Games WHERE id = :gameId")
    fun getGameById(gameId: Long): Single<Game?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewGame(game: Game): Single<Long>

}