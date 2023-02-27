package com.capstone.yojo

data class ListItemData(
    val item: String,
    var imgIndex: Int,
    val name : String?,
    val address : String?,
    val latitude : Double?,
    val longitude : Double?,
    val position : Long,
    val keyValue : String?
    ){ constructor() : this("", 0, "", "", 0.0, 0.0, 0, "") }
// 여기에 이름, 주소, 위도, 경도값 추가해서 위시리스트에 박고 (0217 18:24 기준 "itemList" 카테고리)
// 저장된 위시리스트 불러오는 코드 이용해 위시리스트 화면에 띄우고 클릭하면 해당 값들 가져와서 마커로 찍기
