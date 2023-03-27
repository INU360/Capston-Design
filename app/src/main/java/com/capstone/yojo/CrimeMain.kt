package com.capstone.yojo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.yojo.databinding.CrimeMainBinding




class CrimeMain:AppCompatActivity() {

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /* 바인딩 선언 */
        val binding = CrimeMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* 사용자가 입력한 조건의 데이터를 리스트에 담기 위한 뮤터블 문자열 객체 datas 생성 */
        val datas = mutableListOf<String>()

        /* SQLiteOpenHelper 클래스를 이용하여 질의문을 실행하는 SQLiteDatabase 객체 생성 */
        var dbHelper = DBHelper(this)
        val database = dbHelper.readableDatabase

        /* 리사이클러뷰에 보여질 문자열 리스트 생성 */
        val query = "SELECT * FROM crime_TB;" //crime_TB테이블에서 데이터 조회할 쿼리 생성
        var cursor = database.rawQuery(query,null) //커서에 DB내용 담음
        while(cursor.moveToNext()){ //커서에서 하나씩 이동하며 address컬럼값 가져와 뮤터블 문자열 객체인 datas에 대입
            var address = cursor.getString(cursor.getColumnIndex("address"))
            datas.add("${address}")
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MyAdapter(datas,this)
        binding.recyclerView.addItemDecoration(MyDecoration(this))

        /* 검색버튼 클릭시 사용자가 입력한 조건에 맞는 값 리스트에 보이기 */
        /*
        binding.btnWhole.setOnClickListener {
            val dong = binding.editDong.text.toString() //사용자가 입력한 동 값 가져오기

            /* 사용자가 입력한 조건의 데이터를 리스트에 담기 위한 뮤터블 문자열 객체 datas 생성 */
            val datas = mutableListOf<String>()

            /* SQLiteOpenHelper 클래스를 이용하여 질의문을 실행하는 SQLiteDatabase 객체 생성 */
            var dbHelper = DBHelper(this)
            val database = dbHelper.readableDatabase

            /* 리사이클러뷰에 보여질 문자열 리스트 생성 */
            val query = "SELECT * FROM crime_TB;" //crime_TB테이블에서 데이터 조회할 쿼리 생성
            var cursor = database.rawQuery(query,null) //커서에 DB내용 담음
            while(cursor.moveToNext()){ //커서에서 하나씩 이동하며 address컬럼값 가져와 뮤터블 문자열 객체인 datas에 대입
                var address = cursor.getString(cursor.getColumnIndex("address"))
                datas.add("${address}")
            }

            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = MyAdapter(datas,this)
            binding.recyclerView.addItemDecoration(MyDecoration(this))
        }*/

    }
}