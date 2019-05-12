package bg.tusofia.pmu.museumhunt.util.location

import android.location.Location
import bg.tusofia.pmu.museumhunt.domain.repository.LocationCoordinates

fun LocationCoordinates.toLocation(provider: String = "") = Location(provider).also { location ->
    location.latitude = latitude
    location.longitude = longitude
}