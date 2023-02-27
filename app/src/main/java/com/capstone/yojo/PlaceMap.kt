package com.capstone.yojo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.yojo.databinding.PlaceMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class PlaceMap : AppCompatActivity(), OnMapReadyCallback {

    /* 바인딩 전역 변수 선언 */
    private lateinit var mMap: GoogleMap
    private lateinit var tabLayout: TabLayout

    private var mbinding: PlaceMapBinding? = null
    private val binding get() = mbinding!!

    private var placeMap: HashMap<String, MutableList<PlaceData>> = HashMap()

    /*
    // 마커 추가
    private lateinit var addedBakeryMarker: Marker
    private var addedBakeryMarkerList = mutableListOf<Marker>()


    private lateinit var addedBankMarker: Marker
    private var addedBankMarkerList = mutableListOf<Marker>()

    private lateinit var addedBaryMarker: Marker
    private var addedBaryMarkerList = mutableListOf<Marker>()


    private lateinit var addedCafeMarkerMarker: Marker
    private var addedCafeMarkerList = mutableListOf<Marker>()

    private lateinit var addedCenterMarker: Marker
    private var addedCenterMarkerList = mutableListOf<Marker>()

    private lateinit var addedCoinMarker: Marker
    private var addedCoinMarkerList = mutableListOf<Marker>()

    private lateinit var addedConvMarker: Marker
    private var addedConvMarkerList = mutableListOf<Marker>()

    private lateinit var addedHospitalMarker: Marker
    private var addedHospitaMarkerList = mutableListOf<Marker>()

    private lateinit var addedKidsMarker: Marker
    private var addedKidsMarkerList = mutableListOf<Marker>()

    private lateinit var addedLaundryMarker: Marker
    private var addedLaundryMarkerList = mutableListOf<Marker>()

    private lateinit var addedMartMarker: Marker
    private var addedMartMarkerList = mutableListOf<Marker>()

    private lateinit var addedOutletMarker: Marker
    private var addedOutletMarkerList = mutableListOf<Marker>()

    private lateinit var addedPCMarker: Marker
    private var addedPCMarkerList = mutableListOf<Marker>()

    private lateinit var addedPharmMarker: Marker
    private var addedPharmMarkerList = mutableListOf<Marker>()

    private lateinit var addedSchoolMarker: Marker
    private var addedSchoolMarkerList = mutableListOf<Marker>()

    private lateinit var addedSilverMarker: Marker
    private var addedSilverMarkerList = mutableListOf<Marker>()

    private lateinit var addedTheaterMarker: Marker
    private var addedTheaterMarkerList = mutableListOf<Marker>()


    private lateinit var addedFoodMarker: Marker
    private var addedFoodMarkerList = mutableListOf<Marker>()
    */

    private val yeonsu = LatLng(37.410097, 126.678560)





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placeMap = loadPlaceMap() ?: hashMapOf()

        /* 지도 띄우기 */
        mbinding = PlaceMapBinding.inflate(layoutInflater)
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.placeMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setContentView(binding.root)

        tabLayout = binding.placeTabs

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val type = tab.text.toString() // 선택된 탭아이템 이름 가져옴

                showMarkerForType(type, placeMap) // 선택된 탭아이템에 마커 표시하게 함수 호출
            }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })

        val hospitalData = placeMap["Hospital"]
        if (hospitalData != null && hospitalData.isNotEmpty()) {
            showMarkerForType("Hospital", placeMap)
        }



    }


    // 장소들 가져오기 (placeMap)
    private fun loadPlaceMap(): HashMap<String, MutableList<PlaceData>>? {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("my_place", null)
        val type = object : TypeToken<HashMap<String, MutableList<PlaceData>>>() {}.type
        return Gson().fromJson(json, type)
    }

    /*
    private fun makeFoodMarker() {
        val food = "Food"
        val foodList = placeMap[food]
    }
     */


    private fun showMarkerForType(type: String, placeMap: HashMap<String, MutableList<PlaceData>>) {
        // 기존 마커 지우기
        //mMap.clear()

        // 각각의 TabItem 눌렸을 때 해당 마커 만들기
        val markerOptions = when (type) {
            "병원" -> {
                val hospitalData = placeMap["Hospital"]
                if (hospitalData != null && hospitalData.isNotEmpty()) {
                    val firstHospital = hospitalData[0]
                    MarkerOptions().position(LatLng(firstHospital.latitude!!, firstHospital.longitude!!)).title(firstHospital.name)
                } else {
                    null
                }
            }
            "Park" -> MarkerOptions().position(LatLng(37.6, -122.3)).title("Park")

            else -> null

        }

        // 지도에 마커 띄우기
        if (markerOptions != null) {
            mMap.addMarker(markerOptions)
            val cameraPosition = CameraPosition.Builder().target(markerOptions.position).zoom(13.0f).build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yeonsu))

        // 카메라 이동하기
        val cameraPosition = CameraPosition.builder().target(yeonsu).zoom(12.0f).zoom(12.0f).build()

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        googleMap.moveCamera(cameraUpdate)

    }
}
