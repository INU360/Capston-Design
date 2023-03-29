package com.capstone.yojo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.yojo.databinding.RecommendSubBinding

class RecommendSub : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 바인딩 선언 */
        val binding = RecommendSubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //최소점수(적합한 동네)인덱스 받아오기
        var result = intent.getStringExtra("index")

        if (result.equals("0")) {
            binding.tv.setText("송도동을 추천합니다.")
            binding.tv2.setText("송도동 보러가기")
        }
        else if (result.equals("1")) {
            binding.tv.setText("동춘동을 추천합니다.")
            binding.tv2.setText("동춘동 보러가기")
        }
        else if (result.equals("2")) {
            binding.tv.setText("청학동을 추천합니다.")
            binding.tv2.setText("청학동 보러가기")
        }
        else if (result.equals("3")) {
            binding.tv.setText("옥련동을 추천합니다.")
            binding.tv2.setText("옥련동 보러가기")
        }
        else if (result.equals("4")) {
            binding.tv.setText("연수동을 추천합니다.")
            binding.tv2.setText("연수동 보러가기")
        }
        else {
            binding.tv.setText("선학동을 추천합니다.")
            binding.tv2.setText("선학동 보러가기")
        }
        /* 이미지 클릭 이벤트 - 화면전환 */
        binding.img.setOnClickListener {
            val intent = Intent(this, DongCompare::class.java)
            intent.putExtra("result",result)
            startActivity(intent)
        }
    }


}