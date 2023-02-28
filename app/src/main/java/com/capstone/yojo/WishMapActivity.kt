package com.capstone.yojo


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.capstone.yojo.databinding.WishMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class WishMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mbinding: WishMapBinding? = null
    private val binding get() = mbinding!!

    private lateinit var mMap: GoogleMap
    //private val yeonsu = LatLng(37.410097, 126.678560)
    private lateinit var cardView: View

    private var name: String? = null
    private var address: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* 바인딩 생성 */
        mbinding = WishMapBinding.inflate(layoutInflater)


        name = intent.getStringExtra("name")
        address = intent.getStringExtra("address")
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)



        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.wish_map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            val location = LatLng(latitude!!, longitude!!)
            val markerName = findViewById<TextView>(R.id.wish_map_name)
            val markerAddress = findViewById<TextView>(R.id.wish_map_address)

            markerName.text = name
            markerAddress.text = address

            googleMap.addMarker(MarkerOptions().position(location))
            val cameraPosition = CameraPosition.builder().target(location).zoom(15.0f).zoom(15.0f).build()

            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

            googleMap.moveCamera(cameraUpdate)



            googleMap.setOnMarkerClickListener {

                cardView.visibility = View.VISIBLE

                false
            }

            // 02. 08
            //마커 클릭해서 카드뷰 띄우고 맵 클릭했을 때 카드뷰 없앰
            googleMap.setOnMapClickListener { cardView.visibility = View.GONE }
        }

        setContentView(binding.root)

        cardView = findViewById(R.id.cardview)
        cardView.visibility = View.VISIBLE


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

/*
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yeonsu))

        // 카메라 이동하기
        val cameraPosition = CameraPosition.builder().target(yeonsu).zoom(12.0f).zoom(12.0f).build()

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        googleMap.moveCamera(cameraUpdate)
        val marker = MarkerOptions()

        marker.position(LatLng(latitude!!,longitude!!))



        googleMap.setOnMarkerClickListener { marker ->

            cardView.visibility = View.VISIBLE
            val markerName = findViewById<TextView>(R.id.wish_map_name)
            val markerAddress = findViewById<TextView>(R.id.wish_map_address)

            markerName.text = name
            markerAddress.text = address

            false
        }



        // 02. 08
        //마커 클릭해서 카드뷰 띄우고 맵 클릭했을 때 카드뷰 없앰
        googleMap.setOnMapClickListener { cardView.visibility = View.GONE }
        */

    }


    }
