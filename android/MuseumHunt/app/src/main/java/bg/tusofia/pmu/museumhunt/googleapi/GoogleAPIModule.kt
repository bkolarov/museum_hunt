package bg.tusofia.pmu.museumhunt.googleapi

import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides

@Module
class GoogleAPIModule {

    @Provides
    fun provideGoogleApiAvailability() = GoogleApiAvailability.getInstance()

}