package bg.tusofia.pmu.museumhunt.application

import bg.tusofia.pmu.museumhunt.di.AppComponent
import bg.tusofia.pmu.museumhunt.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class MuseumHuntApplication : DaggerApplication() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        RxJavaPlugins.setErrorHandler {
            Timber.e(it)
        }
    }

    override fun applicationInjector(): AndroidInjector<MuseumHuntApplication> {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        appComponent.inject(this)

        return appComponent
    }
}