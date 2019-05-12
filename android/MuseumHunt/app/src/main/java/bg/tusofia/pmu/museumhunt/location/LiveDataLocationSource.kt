package bg.tusofia.pmu.museumhunt.location

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.LocationSource

class LiveDataLocationSource(private val locationSourceLiveData: LiveData<Location>) : LocationSource {

    private var observer: Observer<Location>? = null

    override fun deactivate() {
        observer?.let { locationSourceLiveData.removeObserver(it) }
    }

    override fun activate(onLocationChangedListener: LocationSource.OnLocationChangedListener?) {
        observer = Observer {
            onLocationChangedListener?.onLocationChanged(it)
        }

        observer?.let {
            locationSourceLiveData.observeForever(it)
        }
    }
}