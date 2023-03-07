package com.capstone.yojo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstone.yojo.databinding.BottomListItemListBinding
import com.capstone.yojo.databinding.BottomSheetBinding
import com.capstone.yojo.databinding.HouseMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class HouseMap : AppCompatActivity(), OnMapReadyCallback {


    //private  var mMap: GoogleMap? = null
    private lateinit var mMap: GoogleMap

    /* 바인딩 전역 변수 선언 */
    private var mbinding: HouseMapBinding? = null
    private val binding get() = mbinding!!

    var houseType = "" //사용자가 선택한 매물 유형(아파트 or 오피스텔)을 담을 변수

    //02.14 메인에서 오피스텔 리스트들 가져오기
    //var OpiMarkerList = arrayListOf<OpiMarkerData>()
    //var OpiLatLngList = arrayListOf<LatLng>()

    // 오피스텔 마커 추가
    private lateinit var addedOpiMarker: Marker
    private var addedOpiMarkerList = mutableListOf<Marker>()

    // 아파트 마커 추가
    private lateinit var addedAptMarker: Marker
    private var addedAptMarkerList = mutableListOf<Marker>()

    // 02.08
    // 카드뷰 변수 선언
    private lateinit var cardView: View

    // 02.08 21:02 마커 클릭이벤트에서 빼온거
    private var keyValue: String? = null

    // 02. 08 20:48 load 함수에서 빼온거

    // 건물명을 키값으로 가지고, 해당 건물의 이름,주소,위도,경도 저장함
    private var infoMap = mutableMapOf<String, MutableList<SaveWishData>>()

    // 마커 찍기용 데이터 들어있음 (이름, 주소, 위도, 경도 등등)
    private var opiMarkerList : ArrayList<OpiMarkerData> = ArrayList()
    private var aptMarkerList : ArrayList<OpiMarkerData> = ArrayList()

    // 거래유형 / 면적 /  가격 / 층 정보가  key : value 형태로 저장되어있음
    private var detailsMap: HashMap<String, MutableList<DetailsData>> = HashMap()
    private var detailsAptMap: HashMap<String, MutableList<DetailsData>> = HashMap()



    // 02. 12 어댑터 바인딩 위해 선언
    private lateinit var bSheetBinding: BottomSheetBinding
    //private lateinit var bListBinding : BottomListItemListBinding

    // 대표 마커 (오피스텔 또는 아파트 위치 찍는 마커) 의 정보 [이름, 주소, 위도, 경도] 담아서 어댑터로 보냄
    val saveList : ArrayList<SaveWishData> = ArrayList()

    // 02. 12 위시리스트 버튼 0 , 1 값 지정 위해
    //var imgIndex = 0

    private val yeonsu = LatLng(37.410097, 126.678560)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* 바인딩 생성 */
        mbinding = HouseMapBinding.inflate(layoutInflater)

        /* MainActivity에서 사용자가 선택한 매물 유형 값(아파트 or 오피스텔) 가져옴 */
        houseType = intent.getStringExtra("houseType").toString()

        // SharedPreferences 에 저장된 opiMarkerList 가져옴
        opiMarkerList = loadOpiMarkerList()
        aptMarkerList = loadAptMarkerList()

        // SharedPreferences 에 저장된 detailsMap 가져옴
        detailsMap = loadDetailsMap() ?: hashMapOf()
        detailsAptMap = loadDetailsAptMap() ?: hashMapOf()


        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        (supportFragmentManager.findFragmentById(R.id.mapView) as
                SupportMapFragment?)!!.getMapAsync(this)
        setContentView(binding.root)

        cardView = findViewById(R.id.cardview)
        //초기 구동시 카드뷰 안보인채로
        cardView.visibility = View.GONE

        // 카드뷰에 온클릭 리스너 적용해서 bottom_sheet가 아닌
        // 다른 액티비티 호출하는게 나을듯
        // 여기서 클릭했을 때 배열 키값과 마커 키값 비교후 같은거 리스트뷰에추가
        cardView.setOnClickListener {
            if(houseType == "오피스텔") {
                getOpiData(keyValue)
            }
            else {
                getAptData(keyValue)
            }
        }

        /* 필터 버튼 클릭 이벤트 - 화면 변환 (양방향 전달)  */
        binding.btnFilter.setOnClickListener {
            val intent = Intent(this, HouseFilter::class.java) //필터화면으로 이동
            startActivityForResult(intent,10) //HouseFilter에서 값 가져오면 함수 호출
        }


    } //onCreate 끝

    // 저장된 오피스텔 마커리스트 가져오기
    fun loadOpiMarkerList(): ArrayList<OpiMarkerData> {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("opi_marker_list", null)
        val type = object : TypeToken<ArrayList<OpiMarkerData>>() {}.type
        return Gson().fromJson(json, type)
    }

    // 저장된 아파트 마커리스트 가져오기
    fun loadAptMarkerList(): ArrayList<OpiMarkerData> {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("apt_marker_list", null)
        val type = object : TypeToken<ArrayList<OpiMarkerData>>() {}.type
        return Gson().fromJson(json, type)
    }



    // 오피스텔 detailsMap 가져오기
    fun loadDetailsMap(): HashMap<String, MutableList<DetailsData>>? {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("details_map", null)
        val type = object : TypeToken<HashMap<String, MutableList<DetailsData>>>() {}.type
        return Gson().fromJson(json, type)
    }

  // 아파트 detailsAptMap 가져오기
  fun loadDetailsAptMap(): HashMap<String, MutableList<DetailsData>>? {
      val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
      val json = prefs.getString("details_apt_map", null)
      val type = object : TypeToken<HashMap<String, MutableList<DetailsData>>>() {}.type
      return Gson().fromJson(json, type)
  }




    private fun addOpiMarker(opiMarkerList:ArrayList<OpiMarkerData>?) {
        if (opiMarkerList != null) {
            for (markerData in opiMarkerList) {
                val marker = MarkerOptions()
                marker.position(LatLng(markerData.latitude!!, markerData.longitude!!))
                addedOpiMarker = mMap.addMarker(marker)!! // addedMarker 이라는 변수는 지도에 마커 추가하는 변수
                addedOpiMarker.isVisible = true
                addedOpiMarker.tag = markerData

                addedOpiMarkerList.add(addedOpiMarker) // 위에서 선언한 addedMarkerList 에 추가한 마커들을 담음

            }
        }

    }

    private fun addAptMarker(aptMarkerList:ArrayList<OpiMarkerData>?) {
        if (aptMarkerList != null) {
            for (markerData in aptMarkerList) {
                val marker = MarkerOptions()
                marker.position(LatLng(markerData.latitude!!, markerData.longitude!!))
                addedAptMarker = mMap.addMarker(marker)!!
                addedAptMarker.isVisible = true
                addedAptMarker.tag = markerData

                addedAptMarkerList.add(addedAptMarker)

            }
        }

    }


    fun getOpiData(keyValue: String?) {
        val list = ArrayList<String>()
        val showDetails = detailsMap[keyValue]
        val mSquare = "\u33a1" // 제곱미터 특수문자 사용
        val enter = "\n" // 줄바꿈




        for (k in showDetails!!) {
            val putType = k.type
            val putSize = k.size
            val putPrice = k.price
            val putFloor = k.floor
            val putDate = k.date
            val putDeposit = k.deposit
            val putMonthly = k.monthly

            if(putPrice != 0L) {
                val addText = " $putType / $putSize $mSquare / $putFloor 층 $enter $putPrice 만원 $enter 계약년월 : $putDate "

                list.add(addText)
            }
            else if(putMonthly == 0L) {
                val addText = " $putType / $putSize $mSquare / $putFloor 층 $enter $putDeposit 만원 $enter 계약년월 : $putDate "

                list.add(addText)
            }
            else {
                val addText = " $putType / $putSize $mSquare / $putFloor 층 $enter $putDeposit / $putMonthly $enter 계약년월 : $putDate"

                list.add(addText)
            }
        }

        /* 여기서 쌓인 정보들을 토대로 리스트뷰 값들의 내용 (list에 해당하는것 + 마커의 정보 [이름,주소,위도,경도]) 를 보내
           해당 포지션의 리스트뷰의 하트 버튼이 클릭되면 writeFirebase 함수에 널값이 아닌 제대로된 값들을 다이렉트로 전달시킴
           >> ListViewAdatper 클래스에서 처리함.
         */


        // bottomSheet 리스트뷰에 띄워줌
        val bottomSheetDialog = BottomSheetDialog(this)

        //val bottomSheetView = LayoutInflater.from(applicationContext).inflate(R.layout.bottom_sheet, null)

        bSheetBinding = BottomSheetBinding.inflate(layoutInflater)

        val adapter = ListViewAdapter(this, list, infoMap, keyValue, bottomSheetDialog)
        val listView = bSheetBinding.listView
        listView.adapter = adapter
        adapter.notifyDataSetChanged()


        // 리스트뷰 내용 보여줌
        bottomSheetDialog.setContentView(bSheetBinding.root)
        bottomSheetDialog.show()


    }

    fun getAptData(keyValue: String?) {
        val list = ArrayList<String>()
        val showDetails = detailsAptMap[keyValue]
        val mSquare = "\u33a1" // 제곱미터 특수문자 사용
        val enter = "\n" // 줄바꿈




        for (k in showDetails!!) {
            val putType = k.type
            val putSize = k.size
            val putPrice = k.price
            val putFloor = k.floor
            val putDate = k.date
            val putDeposit = k.deposit
            val putMonthly = k.monthly

            if(putPrice != 0L) {
                val addText = " $putType / $putSize $mSquare / $putFloor 층 $enter $putPrice 만원 $enter 계약년월 : $putDate "

                list.add(addText)
            }
            else if(putMonthly == 0L) {
                val addText = " $putType / $putSize $mSquare / $putFloor 층 $enter $putDeposit 만원 $enter 계약년월 : $putDate "

                list.add(addText)
            }
            else {
                val addText = " $putType / $putSize $mSquare / $putFloor 층 $enter $putDeposit / $putMonthly $enter 계약년월 : $putDate"

                list.add(addText)
            }
        }

        /* 여기서 쌓인 정보들을 토대로 리스트뷰 값들의 내용 (list에 해당하는것 + 마커의 정보 [이름,주소,위도,경도]) 를 보내
           해당 포지션의 리스트뷰의 하트 버튼이 클릭되면 writeFirebase 함수에 널값이 아닌 제대로된 값들을 다이렉트로 전달시킴
           >> ListViewAdatper 클래스에서 처리함.
         */


        // bottomSheet 리스트뷰에 띄워줌
        val bottomSheetDialog = BottomSheetDialog(this)

        //val bottomSheetView = LayoutInflater.from(applicationContext).inflate(R.layout.bottom_sheet, null)

        bSheetBinding = BottomSheetBinding.inflate(layoutInflater)

        val adapter = ListViewAdapter(this, list, infoMap, keyValue, bottomSheetDialog)
        val listView = bSheetBinding.listView
        listView.adapter = adapter
        adapter.notifyDataSetChanged()


        // 리스트뷰 내용 보여줌
        bottomSheetDialog.setContentView(bSheetBinding.root)
        bottomSheetDialog.show()


    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        // mMap 이 초기화 된 후에 동작해야하므로 마커 찍는 이벤트 등 지도관련은 여기서 처리함.
        if(houseType == "오피스텔") {

            addOpiMarker(opiMarkerList)
        }

        else {
            addAptMarker(aptMarkerList)
        }


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(yeonsu))

        // 카메라 이동하기
        val cameraPosition = CameraPosition.builder().target(yeonsu).zoom(12.0f).zoom(12.0f).build()

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        googleMap.moveCamera(cameraUpdate)


        googleMap.setOnMarkerClickListener { marker ->

            cardView.visibility = View.VISIBLE
            val markerName = findViewById<TextView>(R.id.name)
            val markerAddress = findViewById<TextView>(R.id.address)

            val markerData = marker.tag as OpiMarkerData
            markerName.text = markerData.name
            markerAddress.text = markerData.address

            val saveName = markerData.name
            val saveAddress = markerData.address
            val saveLat = markerData.latitude
            val saveLon = markerData.longitude

            // 전역으로 선언
            keyValue = markerData.keyValue
            saveList.add(SaveWishData(saveName, saveAddress, saveLat, saveLon))
            infoMap[keyValue!!] = saveList

            false
        }

    // 02. 08
    //마커 클릭해서 카드뷰 띄우고 맵 클릭했을 때 카드뷰 없앰
    googleMap.setOnMapClickListener { cardView.visibility = View.GONE }

    }

/* houseFilter 화면에서 사용자가 선택한 값들 가져와 이벤트 처리 */
@SuppressLint("ResourceAsColor")
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
super.onActivityResult(requestCode, resultCode, data)
var resultType = data?.getStringExtra("resultType") //사용자가 선택한 거래유형
var resultArea = data?.getStringExtra("resultArea") //사용자가 선택한 면적
var resultYear = data?.getStringExtra("resultYear") //사용자가 선택한 준공년도
var resultPriceStart = data?.getStringExtra("resultPriceStart") //사용자가 입력한 최소금액(단위:만원)
var resultPriceFinish = data?.getStringExtra("resultPriceFinish") //사용자가 입력한 최대금액(단위:만원)
var result = houseType + "/" + resultPriceStart +"~" + resultPriceFinish + "만원/" + resultType + "/" + resultArea + "/" + resultYear





// 필터링 된 값으로 검색 버튼 텍스트 변경
binding.btnSearch.setTextColor(ContextCompat.getColor(applicationContext!!,R.color.black))
binding.btnSearch.setText(result)

}


}

