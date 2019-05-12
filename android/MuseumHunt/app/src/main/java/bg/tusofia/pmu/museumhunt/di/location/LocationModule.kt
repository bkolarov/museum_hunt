package bg.tusofia.pmu.museumhunt.di.location

import android.content.Context
import bg.tusofia.pmu.museumhunt.location.LocationService
import bg.tusofia.pmu.museumhunt.location.LocationServiceImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import dagger.Module
import dagger.Provides

@Module
class LocationModule {

    @Provides
    fun provideFusedLocationProviderClient(context: Context) = FusedLocationProviderClient(context)

    @Provides
    fun provideLocationServicesSettingsClient(context: Context): SettingsClient = LocationServices.getSettingsClient(context)

    @Provides
    fun provideLocationService(
        fusedLocationClient: FusedLocationProviderClient
    ): LocationService = LocationServiceImpl(fusedLocationClient)

}