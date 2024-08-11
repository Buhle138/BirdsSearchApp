package com.example.birdssearchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private  var mGoogleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

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