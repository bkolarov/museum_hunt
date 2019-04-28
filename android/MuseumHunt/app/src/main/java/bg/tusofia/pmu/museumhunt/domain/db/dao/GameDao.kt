package bg.tusofia.pmu.museumhunt.domain.db.dao

import androidx.room.Dao
import androidx.room.Query
import bg.tusofia.pmu.museumhunt.domain.db.TableConstants
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import io.reactivex.Single

@Dao
interface GameDao {

    @Query("SELECT * FROM " + TableConstants.GAMES)
    fun getAllGames(): Single<List<Game>>

}