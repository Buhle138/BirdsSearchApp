package com.example.birdssearchapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import okhttp3.OkHttpClient
import java.security.Provider
import kotlin.math.log

class MainActivity : AppCompatActivity(), OnMapReadyCallback {



    private  var mGoogleMap: GoogleMap? = null
    private lateinit var autocompleteFragment:AutocompleteSupportFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this)[BirdsApiViewModel::class.java]



        Places.initialize(applicationContext,getString(R.string.google_map_api_key))

        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autoComplete_fragment) as AutocompleteSupportFragment

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object :PlaceSelectionListener{
            override fun onError(p0: Status) {
               Toast.makeText(this@MainActivity, "Some Error in search ${p0}", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                place.latLng?.let { place.latLng?.let { it1 -> viewModel.fetchBirds(it.latitude, it1.longitude) } }



               val add = place.address
                val id = place.id
                val latlng = place.latLng!!
                val marker = addMarker(latlng)



                marker.title = "$add"
                marker.snippet = "$id"

                zoomOnMap(latlng)
            }

        })


        mapFragment.getMapAsync(this)

        viewModel.state.observe(this) { state ->
            mGoogleMap?.clear() // Clear existing markers if needed
            for (item in state.listOfBirds) {
                mGoogleMap?.addMarker(MarkerOptions()
                    .position(LatLng(item.lat, item.lng))
                    .title(item.locName))
                    ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.bird))
            }

            //Add draggable marker
            mGoogleMap?.addMarker(MarkerOptions()
                .position(LatLng(-26.20, 28.04))
                .title("Draggable Marker")
                .draggable(true)
            )
        }

        val mapOptionButton: ImageButton = findViewById(R.id.mapOptionsMenu)

        val popupMenu = PopupMenu(this, mapOptionButton)

        popupMenu.menuInflater.inflate(R.menu.map_options, popupMenu.menu )

        popupMenu.setOnMenuItemClickListener {menuItem ->
            changeMap(menuItem.itemId)
            true
        }

        mapOptionButton.setOnClickListener {
            popupMenu.show()
        }

    }


    @SuppressLint("SuspiciousIndentation")
    private  fun zoomOnMap(latLng: LatLng){
        val newLatLng = CameraUpdateFactory.newLatLngZoom(latLng, 12f)
            mGoogleMap?.animateCamera(newLatLng)
    }

    private fun changeMap(itemId: Int){
        when(itemId) {
            R.id.normal -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            R.id.hybrid -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            R.id.satellite -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            R.id.terrain -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap


        addMarker(LatLng(13.123, 12.123))

        addGragabbleMarker(LatLng(12.456, 14.765))

        mGoogleMap?.addMarker(MarkerOptions()
            .position(LatLng(12.321, 13.432))
            .title("Marker")
        )



        //custom markker
//        mGoogleMap?.addMarker(MarkerOptions()
//            .position(LatLng(12.987, 14.345))
//            .title("Custom Marker")
//            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
//        )

    }
    @SuppressLint("SuspiciousIndentation")
    private fun addMarker(position: LatLng): Marker{
      val marker =   mGoogleMap?.addMarker(MarkerOptions()
            .position((position))
            .title("Marker")
        )

        return marker!!
    }

    private fun addGragabbleMarker(position: LatLng){
        mGoogleMap?.addMarker(MarkerOptions()
            .position((position))
            .title("Draggable Marker")
        )
    }

    //drawing polylines on google maps
    private  fun drawLines() {
        val polyline = mGoogleMap?.addPolyline(PolylineOptions()
            .clickable(true)
            .add(
                LatLng(-26.20, 28.04),
                LatLng(-29.12, 26.21),
                LatLng(-26.20, 28.04),
            )
            .endCap(RoundCap())
            .startCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.menu_bar)))
            .color(ContextCompat.getColor(this, R.color.blue))
            .jointType(JointType.ROUND)
            .width(12f)

        )
    }


}

