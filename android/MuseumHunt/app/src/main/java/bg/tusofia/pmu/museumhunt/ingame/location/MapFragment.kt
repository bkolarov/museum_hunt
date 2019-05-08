package bg.tusofia.pmu.museumhunt.ingame.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.fragment.BaseFragment
import bg.tusofia.pmu.museumhunt.databinding.FragmentMapBinding
import bg.tusofia.pmu.museumhunt.util.maps.addTo
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import permissions.dispatcher.*

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

        observeViewModel()

        setupMap()

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

        viewModel.initWithLevelId(input.levelId)
    }

    private fun setupMap() {
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
    }

    @SuppressLint("MissingPermission")
    private fun observeViewModel() {
        viewModel.showDestinationEvent.observe(viewLifecycleOwner, Observer {
            val latLng = LatLng(it.latitude, it.longitude)
            val camUpdate = CameraUpdateFactory.newLatLng(latLng)

            map.animateCamera(camUpdate)

            MarkerOptions().apply {
                position(latLng)
            }.addTo(map)
        })

        viewModel.showMyLocationEvent.observe(viewLifecycleOwner, Observer {
            map.isMyLocationEnabled = it
            map.uiSettings.isMyLocationButtonEnabled = it
        })

        viewModel.openMaps.observe(viewLifecycleOwner, Observer { uri ->
            Intent(Intent.ACTION_VIEW).apply {
                data = uri
            }.start()
        })

        viewModel.requestLocationPermissionEvent.observe(viewLifecycleOwner, Observer {
            requestLocationPermissionWithPermissionCheck()
        })
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        map.moveCamera(CameraUpdateFactory.zoomTo(initZoomLevel))

        viewModel.onMapReady()
    }

    @NeedsPermission(value = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun requestLocationPermission() {
        viewModel.onLocationPermissionGranted(true)
    }

    @OnShowRationale(value = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun showRationaleForLocation(request: PermissionRequest) {
        showRationaleDialog(R.string.permission_location_rationale, request)
    }

    @OnPermissionDenied(value = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun onLocationDenied() {
        viewModel.onLocationPermissionGranted(false)
    }

    @OnNeverAskAgain(value = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun onLocationNeverAskAgain() {
        view?.let {
            Snackbar.make(it, R.string.permission_camera_never_askagain, Snackbar.LENGTH_SHORT)
        }
    }

    private fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        context?.let {
            AlertDialog.Builder(it, R.style.AppTheme_Dialog_InGame)
                .setPositiveButton(R.string.ok) { _, _ -> request.proceed() }
                .setNegativeButton(R.string.cancel) { _, _ -> request.cancel() }
                .setCancelable(false)
                .setMessage(messageResId)
                .show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun instantiateViewModel(): MapViewModel =
        ViewModelProviders.of(this, viewModelFactory)[MapViewModel::class.java]

}