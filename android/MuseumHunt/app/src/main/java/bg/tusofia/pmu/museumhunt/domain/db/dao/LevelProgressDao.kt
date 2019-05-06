package bg.tusofia.pmu.museumhunt.domain.db.dao

import androidx.room.*
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelProgress
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface LevelProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLevelProgress(levelProgress: LevelProgress): Single<Long>

    @Query("UPDATE LevelProgress SET stage = :stage WHERE id = :levelId")
    fun updateLevelStage(levelId: Long, stage: LevelStage): Completable

    @Update
    fun updateLevelProgress(levelProgress: LevelProgress): Completable

    @Query("SELECT * FROM LevelProgress WHERE id = :levelId")
    fun getLevelProgressById(levelId: Long): Single<LevelProgress>
}