package dev.eliseo.jeff.ui.details

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.android.synthetic.main.details_fragment.toolbar
import kotlinx.android.synthetic.main.search_fragment.*

class DetailsFragment : Fragment(), OnMapReadyCallback {

    private val geonameId: Int?
        get() = arguments?.let { DetailsFragmentArgs.fromBundle(it).geonameId }

    private lateinit var viewModel: DetailsViewModel
    private lateinit var adapter: WeatherObservationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
            val imm =
                context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = WeatherObservationListAdapter()
        recyclerViewDetailsStations.adapter = adapter

        viewModel = ViewModelProviders.of(this, geonameId?.let { DetailsViewModelFactory(it) })
            .get(DetailsViewModel::class.java)

        viewModel.titleText.observe(viewLifecycleOwner, Observer {
            toolbar.title = it
            textViewDetailsTitle.text = it
        })

        viewModel.subtitleText.observe(viewLifecycleOwner, Observer {
            textViewDetailsSubtitle.text = it
        })

        viewModel.bodyText.observe(viewLifecycleOwner, Observer {
            textViewDetailsBody.text = it
        })

        viewModel.weatherObservations.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it?.data)
        })

        viewModel.showPlaceholder.observe(viewLifecycleOwner, Observer {
            textViewDetailsPlaceholder.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.temperature.observe(viewLifecycleOwner, Observer {
            temperatureBarDetails.setTemperature(it)
        })

        viewModel.temperatureText.observe(viewLifecycleOwner, Observer {
            textViewDetailsTemperature.text = it
        })

        viewModel.isTemperatureVisible.observe(viewLifecycleOwner, Observer {
            textViewDetailsTemperature.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.cloudText.observe(viewLifecycleOwner, Observer {
            textViewDetailsClouds.text = it
        })

        viewModel.isCloudTextVisible.observe(viewLifecycleOwner, Observer {
            textViewDetailsClouds.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.humidityText.observe(viewLifecycleOwner, Observer {
            chipViewDetailsHumidity.text = it
        })

        viewModel.isHumidityVisible.observe(viewLifecycleOwner, Observer {
            chipViewDetailsHumidity.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.windSpeedText.observe(viewLifecycleOwner, Observer {
            chipViewDetailsWindSpeed.text = it
        })

        viewModel.isWindSpeedVisible.observe(viewLifecycleOwner, Observer {
            chipViewDetailsWindSpeed.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.dewPointText.observe(viewLifecycleOwner, Observer {
            chipViewDetailsDewPoint.text = it
        })

        viewModel.isDewPointVisible.observe(viewLifecycleOwner, Observer {
            chipViewDetailsDewPoint.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapDetailStations) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.uiSettings?.apply {
            isScrollGesturesEnabled = false
            isRotateGesturesEnabled = false
            isMyLocationButtonEnabled = false
            isIndoorLevelPickerEnabled = false
            isCompassEnabled = false
            isMapToolbarEnabled = false
        }

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
