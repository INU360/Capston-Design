package com.capstone.yojo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.capstone.yojo.databinding.BottomSheetBinding
import com.capstone.yojo.databinding.HouseMapBinding
import com.capstone.yojo.model.MarkerData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.util.*

class HouseMap : AppCompatActivity(), OnMapReadyCallback {

    //private  var mMap: GoogleMap? = null
    private lateinit var mMap: GoogleMap

    /* 바인딩 전역 변수 선언 */
    private var mbinding: HouseMapBinding? = null
    private val binding get() = mbinding!!

    var houseType = "" //사용자가 선택한 매물 유형(아파트 or 오피스텔)을 담을 변수

    // 오피스텔 마커 추가
    private val filteredMarkerDataList = MutableLiveData<List<MarkerData>>() //마커를 찍을 데이터를 저장하는 리스트

    // 03 19 아파트 마커 추가
    private val filteredAptMarkerDataList = MutableLiveData<List<MarkerData>>() //마커를 찍을 데이터를 저장하는 리스트

    // 건물명을 키값으로 가지고, 해당 건물의 이름,주소,위도,경도 저장함
    private val yeonsu = LatLng(37.410097, 126.678560)

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* 바인딩 생성 */
        mbinding = HouseMapBinding.inflate(layoutInflater)

        // 처음 들어갔을때 카드뷰 숨김 03. 19
        hideCardView()

        /* MainActivity에서 사용자가 선택한 매물 유형 값(아파트 or 오피스텔) 가져옴 */
        houseType = intent.getStringExtra("houseType").toString()

        // SharedPreferences 에 저장된 opiMarkerList 가져옴
        // TODO: 아파트로 할 경우 아파트 데이터를 로드해야 함
        filteredMarkerDataList.value = loadOpiMarkerDataList()

        // 03 19 SharedPreferences 에 저장된 aptMarkerList 가져옴
        filteredAptMarkerDataList.value = loadAptMarkerDataList()


        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        (supportFragmentManager.findFragmentById(R.id.mapView) as
                SupportMapFragment?)!!.getMapAsync(this)
        setContentView(binding.root)

        with(binding) {
            btnFilter.setOnClickListener {
                val intent = Intent(this@HouseMap, HouseFilter::class.java) //필터화면으로 이동
                startActivityForResult(intent, 10) //HouseFilter에서 값 가져오면 함수 호출
            }
        }

        // filteredMarkerDataList 값이 변경될 때마다 addMarkers 함수를 호출하도록 Observer 등록
        filteredMarkerDataList.observe(this) {
            addMarkers(it)
        }

        filteredAptMarkerDataList.observe(this) {
            addMarkers(it)
        }
    } //onCreate 끝

    // 저장된 오피스텔 마커리스트 가져오기
    private fun loadOpiMarkerDataList(): List<MarkerData> {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("opi_marker_list", null)
        val type = object : TypeToken<ArrayList<MarkerData>>() {}.type
        return Gson().fromJson(json, type)
    }

    // 03 19 저장된 아파트 마커리스트 가져오기
    private fun loadAptMarkerDataList(): List<MarkerData> {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("apt_marker_list", null)
        val type = object : TypeToken<ArrayList<MarkerData>>() {}.type
        return Gson().fromJson(json, type)
    }

    /**
     * 맵에 마커를 추가한다.
     */
    private fun addMarkers(markerDataList: List<MarkerData>) {
        if (!this::mMap.isInitialized) return

        mMap.clear()

        for (markerData in markerDataList) {
            val markerOptions = MarkerOptions()
            markerOptions.position(LatLng(markerData.latitude, markerData.longitude))

            val marker = mMap.addMarker(markerOptions)!! // addedMarker 이라는 변수는 지도에 마커 추가하는 변수
            marker.isVisible = true
            marker.tag = markerData
        }
    }

    /**
     * 마커 클릭시 CardView 를 보여주고, CardView 클릭시 BottomSheet 가 나오도록 Click listener 를 설정한다.
     */
    private fun showCardView(data: MarkerData) = with(binding) {
        name.text = data.name
        address.text = data.address

        cardview.isVisible = true
        cardview.setOnClickListener {
            // bottomSheet 리스트뷰에 띄워줌
            val bottomSheetDialog = BottomSheetDialog(this@HouseMap)

            val bottomSheetBinging = BottomSheetBinding.inflate(layoutInflater)

            val adapter = ListViewAdapter(this@HouseMap, data)
            val listView = bottomSheetBinging.listView
            listView.adapter = adapter

            // 리스트뷰 내용 보여줌
            bottomSheetDialog.setContentView(bottomSheetBinging.root)
            bottomSheetDialog.show()
        }
    }

    /**
     * CardView 를 숨긴다.
     */
    private fun hideCardView() = with(binding.cardview) {
        isVisible = false
        setOnClickListener(null)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // mMap 이 초기화 된 후에 동작해야하므로 마커 찍는 이벤트 등 지도관련은 여기서 처리함.
        if (houseType == "오피스텔") {
            addMarkers(filteredMarkerDataList.value ?: listOf<MarkerData>())

        } else { // 아파트일때
            addMarkers(filteredAptMarkerDataList.value ?: listOf<MarkerData>())
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yeonsu))

        // 카메라 이동하기
        val cameraPosition = CameraPosition.builder().target(yeonsu).zoom(12.0f).zoom(12.0f).build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        googleMap.moveCamera(cameraUpdate)

        googleMap.setOnMarkerClickListener { marker ->
            val data = marker.tag as? MarkerData ?: return@setOnMarkerClickListener false
            showCardView(data)
            false
        }

        // 02. 08
        //마커 클릭해서 카드뷰 띄우고 맵 클릭했을 때 카드뷰 없앰
        googleMap.setOnMapClickListener { hideCardView() }
    }

    /* houseFilter 화면에서 사용자가 선택한 값들 가져와 이벤트 처리 */
    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val resultType = data?.getStringExtra("resultType") //사용자가 선택한 거래유형
        val resultArea = data?.getStringExtra("resultArea") //사용자가 선택한 면적
        val resultYear = data?.getStringExtra("resultYear") //사용자가 선택한 준공년도
        val resultPriceStart = data?.getStringExtra("resultPriceStart") //사용자가 입력한 최소금액(단위:만원)
        val resultPriceFinish = data?.getStringExtra("resultPriceFinish") //사용자가 입력한 최대금액(단위:만원)
        val result =
            houseType + "/" + resultPriceStart + "~" + resultPriceFinish + "만원/" + resultType + "/" + resultArea + "/" + resultYear

        // TODO: 아파트로 할 경우 아파트 데이터를 로드해야 함 아직은 파베 안 올렸으니까...
        var filteredDataList: List<MarkerData>  //필터링 한 데이터를 저장할 리스트

        // 03. 19 오피스텔일때 아파트일때 구분
        if (houseType == "오피스텔") {
            filteredDataList = ArrayList(loadOpiMarkerDataList()) //필터링 한 데이터를 저장할 리스트

        } else { // 아파트일때
            filteredDataList = ArrayList(loadAptMarkerDataList()) //필터링 한 데이터를 저장할 리스트
        }

        // 준공 년도 필터
        if (resultYear != "전체") {
            // Exclusive
            val max = when (resultArea) {
                "5년 이내" -> 5
                "10년 이내" -> 10
                else -> 20
            }

            filteredDataList = filteredDataList.filter { it.build <= max } //filter 키워드 사용
        }

        // 매매, 전세, 월세 필터 타입 기록
        filteredDataList = filteredDataList.filter { it.details.any { it.type == resultType } }
        filteredDataList.forEach {
            it.details = it.details.filter { it.type == resultType }
        }

        // 면적 필터
        if (resultArea != "전체") {
            // Inclusive
            var min: Double = when (resultArea) {
                "10평대" -> 10.0
                "20평대" -> 20.0
                "30평대" -> 30.0
                "40평대" -> 40.0
                else -> 50.0
            }

            // 최대 설정 10평대니까 +10 해서 설정..
            var max: Double = min + 10

            // 평수를 제곱미터로 전환
            min *= 3.3058
            max *= 3.3058

            filteredDataList.forEach {
                it.details = it.details.filter { it.size >= min && it.size < max } //평수 필터링
            }
        }

        // 가격 필터
        val minPrice = resultPriceStart?.toIntOrNull() ?: 0
        val maxPrice = resultPriceFinish?.toIntOrNull() ?: 0

        //일단은 매매는 가격으로 설정했고(price) 전세랑 월세는 파베에 deposit로 다른걸로 묶임
        if (minPrice < maxPrice) {
            if (resultType == "매매") {
                filteredDataList.forEach {
                    it.details = it.details.filter { it.price in minPrice..maxPrice }
                }
            } else {
                filteredDataList.forEach {
                    it.details = it.details.filter { it.deposit in minPrice..maxPrice }
                }
            }
        }

        filteredDataList = filteredDataList.filter { it.details.isNotEmpty() } //공백 아닐때 저장
        filteredMarkerDataList.value = filteredDataList //필터링 한 데이터를 저장하여 마커를 찍는 리스트에 저장완료.

        // 필터링 된 값으로 검색 버튼 텍스트 변경
        binding.btnSearch.setTextColor(ContextCompat.getColor(applicationContext!!, R.color.black))
        binding.btnSearch.text = result

        hideCardView()
    }
}