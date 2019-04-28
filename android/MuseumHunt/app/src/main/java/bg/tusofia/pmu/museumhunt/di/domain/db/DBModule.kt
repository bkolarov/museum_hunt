package bg.tusofia.pmu.museumhunt.di.domain.db

import android.content.Context
import androidx.room.Room
import bg.tusofia.pmu.museumhunt.domain.db.DB_NAME
import bg.tusofia.pmu.museumhunt.domain.db.MuseumHuntRoomDB
import dagger.Module
import dagger.Provides

@Module
class DBModule {

    @Provides
    fun provideRoomDB(context: Context) =
        Room.databaseBuilder(
            context,
            MuseumHuntRoomDB::class.java,
            DB_NAME
        )
            .build()

}