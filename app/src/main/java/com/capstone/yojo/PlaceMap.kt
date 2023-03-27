package com.capstone.yojo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstone.yojo.databinding.PlaceMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
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
    //private var foodMap: HashMap<String, MutableList<PlaceData>> = HashMap()


    private val yeonsu = LatLng(37.410097, 126.678560)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placeMap = loadPlaceMap() ?: hashMapOf()
        //foodMap = loadFoodMap() ?: hashMapOf()


        /* 지도 띄우기 */
        mbinding = PlaceMapBinding.inflate(layoutInflater)
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.placeMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setContentView(binding.root)

        tabLayout = binding.placeTabs

        // placeMap 의 키값 (한글로 수정함) 을 탭 이름으로 + 탭 생성
        for (category in placeMap.keys) {
            val tab = tabLayout.newTab().setText(category)
            //Log.e("category ", category)
            tabLayout.addTab(tab)
        }




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

            // 03 14 시도
            if(category == "병원") {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_marker))
                // 64px 는 큰 감이 있고 32px는 작은감이 있음 48px 정도로 따로 만들면 깔끔 할 듯?
                // 병원은 큰 병원 (병원급 이상만 다루루기..?)
            }
            mMap.addMarker(markerOptions)
        }

    }

    // 03 14 시도
    private fun getResizedBitmap(drawableId: Int, size: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(this, drawableId) ?: return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val bitmap = (drawable as BitmapDrawable).bitmap
        return Bitmap.createScaledBitmap(bitmap, size, size, false)
    }

    private fun getMarkerSize(zoomLevel: Float): Float {
        return when (zoomLevel.toInt()) {
            in 0..10 -> 50f
            in 11..15 -> 100f
            else -> 150f
        }
    }
    // 시도한거 작동 안함... 수정필요


    // 장소들 가져오기 (placeMap)
    private fun loadPlaceMap(): HashMap<String, MutableList<PlaceData>>? {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("my_place", null)
        val type = object : TypeToken<HashMap<String, MutableList<PlaceData>>>() {}.type
        return Gson().fromJson(json, type)
    }

    /*
    private fun loadFoodMap(): HashMap<String, MutableList<PlaceData>>? {
        val prefs = getSharedPreferences("my_food", Context.MODE_PRIVATE)
        val json = prefs.getString("my_food", null)
        val type = object : TypeToken<HashMap<String, MutableList<PlaceData>>>() {}.type
        return Gson().fromJson(json, type)
    }

     */


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 기존 코드는 동네시설 화면 들어갔을때 첫 탭 아이템에 마커 미출력됨
        // 첫번째 탭 선택시키고 마커 찍도록 코드 수정. (지도 초기화 된 후 생성해야하므로 onMapReady 에 작성)
        val firstCategory = placeMap.keys.firstOrNull()
        val firstTab = tabLayout.getTabAt(placeMap.keys.indexOf(firstCategory)) ?: return
        tabLayout.selectTab(firstTab)
        firstCategory?.let { updateMapMarkers(it) }

        // 나머지 탭들 클릭이벤트 (마커 update )
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val category = tab.text.toString()
                updateMapMarkers(category)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yeonsu))

        // 카메라 이동하기
        val cameraPosition = CameraPosition.builder().target(yeonsu).zoom(12.0f).zoom(12.0f).build()

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        googleMap.moveCamera(cameraUpdate)

        // 03 14 시도
        // 줌 레벨 변경 이벤트 처리 (>> 작동안함)
        mMap.setOnCameraIdleListener {
            val category = tabLayout.getTabAt(tabLayout.selectedTabPosition)?.text.toString()
            updateMapMarkers(category)
        }

    }
}