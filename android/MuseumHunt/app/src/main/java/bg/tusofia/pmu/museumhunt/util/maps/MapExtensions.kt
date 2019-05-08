package bg.tusofia.pmu.museumhunt.util.maps

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions

fun MarkerOptions.addTo(map: GoogleMap) {
    map.addMarker(this)
}