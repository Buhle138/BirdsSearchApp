package com.example.birdssearchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private  var mGoogleMap: GoogleMap? = null
    private lateinit var autocompleteFragment:AutocompleteSupportFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Places.initialize(applicationContext,getString(R.string.google_map_api_key))

        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autoComplete_fragment) as AutocompleteSupportFragment

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object :PlaceSelectionListener{
            override fun onError(p0: Status) {
               Toast.makeText(this@MainActivity, "Some Error in search", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
//               val add = place.address
//                val id = place.id
                val latlng = place.latLng!!

                zoomOnMap(latlng)
            }

        })

        mapFragment.getMapAsync(this)

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
    }
}