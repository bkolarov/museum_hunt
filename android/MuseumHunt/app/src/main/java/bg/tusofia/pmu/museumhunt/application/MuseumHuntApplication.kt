package bg.tusofia.pmu.museumhunt.application

import android.app.Application
import timber.log.Timber

class MuseumHuntApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

    }

}