package dev.eliseo.jeff.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import dev.eliseo.jeff.R
import kotlinx.android.synthetic.main.details_fragment.*


class DetailsFragment : Fragment(), OnMapReadyCallback {

    private val geonameId: Int?
        get() = arguments?.let { DetailsFragmentArgs.fromBundle(it).geonameId }

    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, geonameId?.let { DetailsViewModelFactory(it) })
            .get(DetailsViewModel::class.java)

        viewModel.title.observe(viewLifecycleOwner, Observer {
            collapsingToolbar.title = it
        })

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapDetailStations) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.uiSettings?.isScrollGesturesEnabled = false
        googleMap?.uiSettings?.isRotateGesturesEnabled = false
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        googleMap?.uiSettings?.isIndoorLevelPickerEnabled = false
        googleMap?.uiSettings?.isCompassEnabled = false
        googleMap?.uiSettings?.isMapToolbarEnabled = false

        viewModel.mapMarkers.observe(viewLifecycleOwner, Observer { resource ->
            resource.data?.forEach {
                googleMap?.addMarker(it)
            }
        })

        viewModel.mapBounds.observe(viewLifecycleOwner, Observer {
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(it, 16))
        })
    }

}
