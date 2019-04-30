package bg.tusofia.pmu.museumhunt.domain.repository

import io.reactivex.Single

interface LevelDataRepository {

    fun getLevelData(levelNum: Int): Single<LevelData>

}

class LevelDataRepositoryImpl : LevelDataRepository {

    override fun getLevelData(levelNum: Int): Single<LevelData> {
        return try {
            val levelData = when (levelNum) {
                0 -> LevelData(
                    hintWords = listOf("бозайник", "фосил", "минерал", "биология", "природа"),
                    riddle = "Кой е най-старият музей в България?",
                    riddleAnswers = listOf(
                        "Музей по палеонтология и исторична геология",
                        " Музей на съвременното изкуство",
                        "Национален природонаучен музей при БАН",
                        "Регионален исторически музей – София"
                    ),
                    location = LocationCoordinates(42.6558745, 23.2654635)
                )
                1 -> LevelData(
                    hintWords = listOf("история", "софия", "панагюрско", "съкровище"),
                    riddle = "В кой софийски музей входът е безплатен всеки последен понеделник от месец?",
                    riddleAnswers = listOf(
                        "Национален исторически музей",
                        "Национален етнографски музей",
                        "Национален антропологичен музей",
                        "Национален музей „Боянска църква”"
                    ),
                    location = LocationCoordinates(42.6955959, 23.3262678)
                )
                2 -> LevelData(
                    hintWords = listOf("физическа", "култура", "стоичков", "медал"),
                    riddle = "В кой музей са изложени екипите на Албена Денкова и Максим Ставийски от световното първенство по фигурно пързаляне във Вашингтон (САЩ)?",
                    riddleAnswers = listOf(
                        "Музей на съвременното изкуство",
                        "Музей по история на физиката в България",
                        "Музей на спорта",
                        "Музей на социалистическото изкуство"
                    ),
                    location = LocationCoordinates(42.687435, 23.335202)
                )
                else -> throw IllegalStateException("Wrong number level")
            }
            Single.just(levelData)
        } catch (e: Exception) {
            Single.error<LevelData>(e)
        }
    }
}

data class LevelData(
    val hintWords: List<String>,
    val riddle: String,
    val riddleAnswers: List<String>,
    val location: LocationCoordinates
)

data class LocationCoordinates(val longitude: Double, val latitude: Double)