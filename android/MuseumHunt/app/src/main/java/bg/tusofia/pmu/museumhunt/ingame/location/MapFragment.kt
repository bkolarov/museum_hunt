package bg.tusofia.pmu.museumhunt.ingame.location

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.fragment.BaseFragment
import bg.tusofia.pmu.museumhunt.databinding.FragmentMapBinding
import bg.tusofia.pmu.museumhunt.domain.repository.LocationCoordinates
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MapFragment : BaseFragment<FragmentMapBinding, MapViewModel>(),
    OnMapReadyCallback {

    companion object {
        private const val initZoomLevel = 18f
    }

    private val input: MapFragmentArgs by navArgs()

    private lateinit var map: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentMapBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = GoogleMapOptions()
            .zoomControlsEnabled(false)
            .rotateGesturesEnabled(true)
            .compassEnabled(true)
            .tiltGesturesEnabled(true)

        val mapFragment = childFragmentManager.findFragmentById(R.id.fl_map_container) as? SupportMapFragment
            ?: SupportMapFragment.newInstance(options)

        childFragmentManager.beginTransaction()
            .replace(R.id.fl_map_container, mapFragment)
            .commitNow()

        mapFragment.getMapAsync(this)

        viewModel.showDestinationEvent.observe(viewLifecycleOwner, Observer {
            val camUpdate = CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude))

            map.moveCamera(camUpdate)
        })

        binding { vm ->
            viewModel = vm

            toolbar.inflateMenu(R.menu.menu_toolbar_riddle_fragment)
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.menu_item_help) {
                    vm.onHelpClick()
                }

                true
            }

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener {
                vm.onToolbarNavigationClick()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        map.moveCamera(CameraUpdateFactory.zoomTo(initZoomLevel))

        viewModel.initWithLevelId(input.levelId)
    }

    @NeedsPermission(value = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun showDestinationOnMap(location: LocationCoordinates) {

    }

    override fun instantiateViewModel(): MapViewModel =
        ViewModelProviders.of(this, viewModelFactory)[MapViewModel::class.java]

}