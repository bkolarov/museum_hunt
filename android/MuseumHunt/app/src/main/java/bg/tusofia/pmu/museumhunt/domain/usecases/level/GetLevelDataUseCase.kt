package bg.tusofia.pmu.museumhunt.domain.usecases.level

import bg.tusofia.pmu.museumhunt.domain.repository.LevelDataRepository
import javax.inject.Inject

class GetLevelDataUseCase @Inject constructor(private val levelDataRepository: LevelDataRepository) {

    fun getLevelData(levelNum: Int) = levelDataRepository.getLevelData(levelNum)

}