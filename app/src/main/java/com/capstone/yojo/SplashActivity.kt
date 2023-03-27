package com.capstone.yojo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.yojo.model.MarkerData
import com.google.common.reflect.TypeToken
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStreamReader


class SplashActivity : AppCompatActivity() {


    private val placeMap: HashMap<String, MutableList<PlaceData>> = hashMapOf()
    //private val foodMap : HashMap<String, MutableList<PlaceData>> = hashMapOf()

    private val opiMarkerList: ArrayList<OpiMarkerData> = ArrayList()
    private val aptMarkerList: ArrayList<OpiMarkerData> = ArrayList()

    private val detailsMap: HashMap<String, MutableList<DetailsData>> = hashMapOf()
    private val detailsAptMap: HashMap<String, MutableList<DetailsData>> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // 스플래시 화면에서 데이터 로드 위해 Firebase 초기화
        FirebaseApp.initializeApp(this)

        /*
        // 03 19 시도
        val aptDataSet = readJSONapt()
        saveAptData(aptDataSet)
         */


        // 함수 실행해서 Firebase 에 저장된 데이터 전부 끌어오면 메인액티비티로 넘어감
        fetchDataFromFirebase()
        // if문으로 배열에 데이터 담겨있으면 바로 메인액티비티로 넘어가게 해도 될 듯
    }

    private fun fetchDataFromFirebase() {

        val database = Firebase.database

        val opiReference = database.getReference("OpiMarker") // 오피스텔 카테고리 접근
        val aptReference = database.getReference("AptMarker")
        val apt2Reference = database.getReference("AptMarker2")

        //val foodReference = database.getReference("FoodMarker")
        val placeReference = database.getReference("Marker")


        placeReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 최상위 경로의 자식들에 접근
                for (ds in snapshot.children) {
                    when (ds.key) {

                        "Bakery" -> {
                            val snp = snapshot.child("Bakery")
                            val placeKey = "제과점"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Bank" -> {
                            val snp = snapshot.child("Bank")
                            val placeKey = "은행"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Bar" -> {
                            val snp = snapshot.child("Bar")
                            val placeKey = "단란주점"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Cafe" -> {
                            val snp = snapshot.child("Cafe")
                            val placeKey = "카페"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Center" -> {
                            val snp = snapshot.child("Center")
                            val placeKey = "공공기관"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        // 스냅샷 자식들 중 "Coin" 이면
                        "Coin" -> {
                            // 스냅샷 자식중 Coin
                            val snp = snapshot.child("Coin")
                            val placeKey = "코인노래방"
                            val valueList = mutableListOf<PlaceData>()
                            // coin 안에 있는 항목들 for문으로 전부 가져옴
                            for (value in snp.children) {
                                // 하위 항목들에 각각 접근해 해당 위도 경도 Double형으로 가져옴
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double

                                // 하위 항목들에 각각 접근해 상호명 String형으로 가져옴
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String

                                // 데이터 클래스 선언한 모양으로 변수 만듦
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Convenience" -> {
                            val snp = snapshot.child("Convenience")
                            val placeKey = "편의점"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Hospital" -> {
                            val snp = snapshot.child("Hospital")
                            val placeKey = "병원"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Kids" -> {
                            val snp = snapshot.child("Kids")
                            val placeKey = "어린이집"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Laundry" -> {
                            val snp = snapshot.child("Laundry")
                            val placeKey = "세탁소"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Mart" -> {
                            val snp = snapshot.child("Mart")
                            val placeKey = "마트"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Outlet" -> {
                            val snp = snapshot.child("Outlet")
                            val placeKey = "아울렛"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "PCroom" -> {
                            val snp = snapshot.child("PCroom")
                            val placeKey = "PC방"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Pharmacy" -> {
                            val snp = snapshot.child("Pharmacy")
                            val placeKey = "약국"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "School" -> {
                            val snp = snapshot.child("School")
                            val placeKey = "학교"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                        "Silver" -> {
                            val snp = snapshot.child("Silver")
                            val placeKey = "노인복지"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }


                        "Theater" -> {
                            val snp = snapshot.child("Theater")
                            val placeKey = "영화관"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                val latitude = value.child("latitude").value as Double
                                val longitude = value.child("longitude").value as Double
                                val name = value.child("name").value as String
                                val address = value.child("address").value as String
                                val set = PlaceData(name, address, latitude, longitude)

                                valueList.add(set)
                            }
                            placeMap[placeKey] = valueList
                        }

                    }

                }
                savePlaceMap(placeMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })

        /*
        foodReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 최상위 경로의 자식들에 접근
                for (ds in snapshot.children) {
                    when (ds.key) {
                        "Food" -> {
                            val snp = snapshot.child("Food")
                            val placeKey = "음식점"
                            val valueList = mutableListOf<PlaceData>()
                            for (value in snp.children) {
                                for (dt in value.children) {

                                    val name = value.child("name").value as String?
                                    val address = value.child("address").value as String?
                                    val latitude = value.child("latitude").value as Double?
                                    val longitude = value.child("longitude").value as Double?
                                    val set = PlaceData(name, address, latitude, longitude)

                                    valueList.add(set)
                                }
                                placeMap[placeKey] = valueList
                            }

                        }

                    }

                }
                // SharedPreference 에 placeMap 저장
                saveFoodMap(placeMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })

         */



        opiReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 최상위 경로의 자식들에 접근
                for (ds in snapshot.children) {
                    when (ds.key) {
                        "Opi" -> {
                            // TODO: 아파트 데이터도 아래와 같은 방식으로 불러와서 저장해야 함
                            val opi = snapshot.child("Opi")
                            val dataSet = arrayListOf<MarkerData>()

                            for (value in opi.children) {
                                val data = value.getValue(MarkerData::class.java)?.apply {
                                    // key 값은 아파트 이름
                                    key = value.key!!
                                } ?: continue

                                data.details.forEachIndexed { index, details ->
                                    // key 값은 Index (숫자)
                                    details.key =
                                        value.child("details").children.toList()[index].key!!.toInt()
                                }

                                dataSet.add(data)
                            }

                            getSharedPreferences("my_pref", Context.MODE_PRIVATE)
                                .edit()
                                .putString("opi_marker_list", Gson().toJson(dataSet))
                                .apply()
                        }
                    }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })

        aptReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 최상위 경로의 자식들에 접근
                val dataSet = arrayListOf<MarkerData>()
                for (ds in snapshot.children) {
                    when (ds.key) {
                        "Apt" -> {

                            val apt = snapshot.child("Apt")


                            for (value in apt.children) {
                                val data = value.getValue(MarkerData::class.java)?.apply {
                                    // key 값은 아파트 이름
                                    key = value.key!!
                                } ?: continue

                                data.details.forEachIndexed { index, details ->
                                    // key 값은 Index (숫자)
                                    details.key =
                                        value.child("details").children.toList()[index].key!!.toInt()
                                }

                                dataSet.add(data)
                            }


                        }

                        "Apt2" -> {

                            val apt = snapshot.child("Apt2")
                            //val dataSet = arrayListOf<MarkerData>()

                            for (value in apt.children) {
                                val data = value.getValue(MarkerData::class.java)?.apply {
                                    // key 값은 아파트 이름
                                    key = value.key!!
                                } ?: continue

                                data.details.forEachIndexed { index, details ->
                                    // key 값은 Index (숫자)
                                    details.key =
                                        value.child("details").children.toList()[index].key!!.toInt()
                                }

                                dataSet.add(data)
                            }


                        }

                        "Apt3" -> {

                            val apt = snapshot.child("Apt3")
                            //val dataSet = arrayListOf<MarkerData>()

                            for (value in apt.children) {
                                val data = value.getValue(MarkerData::class.java)?.apply {
                                    // key 값은 아파트 이름
                                    key = value.key!!
                                } ?: continue

                                data.details.forEachIndexed { index, details ->
                                    // key 값은 Index (숫자)
                                    details.key =
                                        value.child("details").children.toList()[index].key!!.toInt()
                                }

                                dataSet.add(data)
                            }



                        }
                    }

                }
                getSharedPreferences("my_pref", Context.MODE_PRIVATE)
                    .edit()
                    .putString("apt_marker_list", Gson().toJson(dataSet))
                    .apply()

                launchMainActivity()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })
    }

    /*
    //03 19 시도
    private fun readJSONapt() : ArrayList<MarkerData> {
        val gson = Gson()
        val assetManager = assets
        val inputStream = assetManager.open("AptClick.json")
        val reader = InputStreamReader(inputStream)

        return try {
            val type = object : TypeToken<HashMap<String, MarkerData>>() {}.type
            val markerDataMap: HashMap<String, MarkerData> = gson.fromJson(reader, type)

            ArrayList<MarkerData>().apply {
                for ((key, markerData) in markerDataMap) {
                    markerData.key = key
                    markerData.details.forEachIndexed { index, details ->
                        details.key = index
                    }
                    add(markerData)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            arrayListOf()
        } finally {
            try {
                reader.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

     */

    /*
    private fun saveAptData(aptDataSet: List<MarkerData>) {
        getSharedPreferences("my_pref", Context.MODE_PRIVATE)
            .edit()
            .putString("apt_marker_list", Gson().toJson(aptDataSet))
            .apply()
    }

     */

    // 장소들 저장 (placeMap)
    private fun savePlaceMap(placeMap: HashMap<String, MutableList<PlaceData>>?) {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        if (placeMap != null) {
            val gson = Gson()
            val json = gson.toJson(placeMap)
            editor.putString("my_place", json)
        } else {
            editor.putString("my_place", null)
        }

        editor.apply()
    }

    /*
    // 음식점 저장 (foodMap)
    private fun saveFoodMap(foodMap: HashMap<String, MutableList<PlaceData>>?) {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        if (foodMap != null) {
            val gson = Gson()
            val json = gson.toJson(foodMap)
            editor.putString("my_food", json)
        } else {
            editor.putString("my_food", null)
        }

        editor.apply()
    }

     */

    /*
    // 장소들 가져오기 (placeMap)
    private fun loadPlaceMap(): HashMap<String, MutableList<PlaceData>>? {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("my_place", null)
        val type = object : TypeToken<HashMap<String, MutableList<PlaceData>>>() {}.type
        return Gson().fromJson(json, type)
    }

     */

    // 오피스텔 마커리스트 저장(opiMarkerList)
    fun saveOpiMarkerList(opiMarkerList: ArrayList<OpiMarkerData>) {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(opiMarkerList)
        editor.putString("opi_marker_list", json)
        editor.apply()
    }

    /*
    // 저장된 오피스텔 마커리스트 가져오기
    fun loadOpiMarkerList(): ArrayList<OpiMarkerData> {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("opi_marker_list", null)
        val type = object : TypeToken<ArrayList<OpiMarkerData>>() {}.type
        return Gson().fromJson(json, type)
    }

     */

    /*
    // 아파트 마커리스트 저장(aptMarkerList)
    fun saveAptMarkerList(aptMarkerList: ArrayList<OpiMarkerData>) {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(aptMarkerList)
        editor.putString("apt_marker_list", json)
        editor.apply()
    }
     */

    /*
    // 저장된 아파트 마커리스트 가져오기
    fun loadAptMarkerList(): ArrayList<OpiMarkerData> {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("apt_marker_list", null)
        val type = object : TypeToken<ArrayList<OpiMarkerData>>() {}.type
        return Gson().fromJson(json, type)
    }

     */

    // 오피스텔 detailsMap 저장
    private fun saveDetailsData(detailsMap: HashMap<*, *>?) {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        if (detailsMap != null) {
            val gson = Gson()
            val json = gson.toJson(detailsMap)
            editor.putString("details_map", json)
        } else {
            editor.putString("details_map", null)
        }
        editor.apply()
    }

    /*
    // 오피스텔 detailsMap 가져오기
    fun loadDetailsMap(): HashMap<String, MutableList<DetailsData>>? {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("details_map", null)
        val type = object : TypeToken<HashMap<String, MutableList<DetailsData>>>() {}.type
        return Gson().fromJson(json, type)
    }

     */

    // 아파트 detailsAptMap 저장
    private fun saveDetailsAptData(detailsAptMap: HashMap<*, *>?) {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        if (detailsAptMap != null) {
            val gson = Gson()
            val json = gson.toJson(detailsAptMap)
            editor.putString("details_apt_map", json)
        } else {
            editor.putString("details_apt_map", null)
        }
        editor.apply()
    }

    /*
    // 아파트 detailsAptMap 가져오기
    fun loadDetailsAptMap(): HashMap<String, MutableList<DetailsData>>? {
        val prefs = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val json = prefs.getString("details_apt_map", null)
        val type = object : TypeToken<HashMap<String, MutableList<DetailsData>>>() {}.type
        return Gson().fromJson(json, type)
    }

     */


    private fun launchMainActivity() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}