package com.capstone.yojo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.yojo.databinding.CrimeSubBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CrimeSub : AppCompatActivity(), OnMapReadyCallback {

    /* 바인딩 전역 변수 선언 */
    private lateinit var mMap: GoogleMap
    private var mbinding: CrimeSubBinding? = null
    private val binding get() = mbinding!!

    /* 전역변수 선언 */
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var addr: String? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 바인딩 생성 */
        mbinding = CrimeSubBinding.inflate(layoutInflater)

        /* SQLiteOpenHelper 클래스를 이용하여 질의문을 실행하는 SQLiteDatabase 객체 생성 */
        var dbHelper = DBHelper(this)
        val database = dbHelper.readableDatabase

        /* 사용자가 클릭한 리사이클러뷰의 포지션 값 가져와서 해당 조건에 맞는 내용 DB에서 찾아 출력 */
        val pos = intent.getIntExtra("pos",0) //사용자가 선택한 리사이클러뷰 아이템 position 값
        val id = pos + 1 //DB에 _id는 1부터 시작하므로 pos+1
        val query = "SELECT * FROM crime_TB WHERE _id==$id;" //crime_TB테이블에서 사용자가 클릭한 거 가져옴
        var cursor = database.rawQuery(query,null) //커서에 DB내용 담음
        while(cursor.moveToNext()){ //사용자 선택 조건에 맞춰 화면 내용 업데이트
            var name = cursor.getString(cursor.getColumnIndex("name"))
            var address = cursor.getString(cursor.getColumnIndex("address"))
            addr = address
            var content = cursor.getString(cursor.getColumnIndex("content"))
            var place = cursor.getString(cursor.getColumnIndex("place"))
            var num = cursor.getString(cursor.getColumnIndex("num"))
            var dong = cursor.getString(cursor.getColumnIndex("dong"))
            var lat = cursor.getDouble(cursor.getColumnIndex("lat"))
            latitude = lat
            var long = cursor.getDouble(cursor.getColumnIndex("long"))
            longitude = long

            binding.tv0.setText("이름: "+name)
            binding.tv1.setText("주소: "+address)
            binding.tv4.setText("내용: "+content)
            binding.tv5.setText("장소: "+place)
            binding.tv7.setText("전과: "+ num)
            binding.tv8.setText("동: "+dong)
        }

        /* 지도 띄우기 */
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setContentView(binding.root)

        (supportFragmentManager.findFragmentById(R.id.mapView) as
                SupportMapFragment?)!!.getMapAsync(this)


    } //onCreate 끝

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latLng = LatLng(latitude!!, longitude!!)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title(addr)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        mMap.addMarker(markerOptions)
    }


}
