package com.capstone.yojo

import android.content.Context
import android.os.Bundle
import android.util.Log
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
    private var foodMap: HashMap<String, MutableList<PlaceData>> = HashMap()


    private val yeonsu = LatLng(37.410097, 126.678560)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placeMap = loadPlaceMap() ?: hashMapOf()
        foodMap = loadFoodMap() ?: hashMapOf()


        /* 지도 띄우기 */
        mbinding = PlaceMapBinding.inflate(layoutInflater)
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.placeMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setContentView(binding.root)

        tabLayout = binding.placeTabs

        // placeMap 의 키값 (한글로 수정함) 을 탭 이름으로 + 탭 생성
        for (category in placeMap.keys) {
            val tab = tabLayout.newTab().setText(category)
            Log.e("category ", category)
            tabLayout.addTab(tab)
        }

        // 탭 눌렀을때 이벤트
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val category = tab.text.toString()

                updateMapMarkers(category)
                /*  음식점 몇천개여서 제외시킴

                if(category == "음식점") {
                    mMap.clear()
                    val places = foodMap[category] ?: return
                    for (place in places) {
                        val markerOptions = MarkerOptions()
                            .position(LatLng(place.latitude!!, place.longitude!!))
                            .title(place.name)
                            .snippet(place.address)
                        mMap.addMarker(markerOptions)
                    }
                }

                 */


            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


    }

    private fun updateMapMarkers(category: String) {
        val places = placeMap[category] ?: return

            // 탭 누를때마다 기존 마커 위에 겹치지 않게 마커 날려줌
            mMap.clear()

            // 각 탭별 새로운 마커 찍음
            for (place in places) {
                val markerOptions = MarkerOptions()
                    .position(LatLng(place.latitude!!, place.longitude!!))
                    .title(place.name)
                    .snippet(place.address)
                mMap.addMarker(markerOptions)
            }

    }


    // 장소들 가져오기 (placeMap)
    private fun loadPlaceMap(): HashMap<String, MutableList<PlaceData>>? {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("my_place", null)
        val type = object : TypeToken<HashMap<String, MutableList<PlaceData>>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun loadFoodMap(): HashMap<String, MutableList<PlaceData>>? {
        val prefs = getSharedPreferences("my_food", Context.MODE_PRIVATE)
        val json = prefs.getString("my_food", null)
        val type = object : TypeToken<HashMap<String, MutableList<PlaceData>>>() {}.type
        return Gson().fromJson(json, type)
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
