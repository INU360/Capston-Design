package com.capstone.yojo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.yojo.databinding.DongCompareBinding

class DongCompare : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*동네추천 화면에서 추천받은 동네 받아오기*/
        var result = intent.getStringExtra("result")

        /* 바인딩 선언 */
        val binding = DongCompareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* '동 버튼' 이벤트 처리  */
        var bindingDong = arrayOf(binding.songdo,binding.dongchun,binding.chunghak,binding.okryeon,
            binding.yeonsoo,binding.sunhak) //동 버튼 바인딩 배열
        //그래프 이미지 담을 변수
        var img = arrayOf(R.drawable.songdo,R.drawable.dongchun,R.drawable.chunghak,
            R.drawable.okryeon,R.drawable.yeonsu,R.drawable.sunhak)
        //해쉬태그 담을 변수
        var tagStr =arrayOf("#자녀 가정 추천  #노인 가정 추천  #1인 가구 추천",
            "#대학생 추천  #1인 가구 추천 #자녀 가정 비추천",
            "#노인 가정 추천",
            "#자녀 가정 비추천",
            "#노인 가정 추천  #자녀 가정 비추천",
            "#모든 가정 추천")
        //동 설명을 담을 변수
        var dongStr =arrayOf("송도동은 교육시설, 노인복지시설, 놀이시설, 쇼핑, 음식점, 의료기관이 모두 다른 동에 비해 많이 위치하고 있습니다. 특히 교육기관, 음식점, 의료기관이 송도동에 많이 집중되어 있습니다.",
            "동춘동의 교육시설과 노인복지시설은 동 평균에 속하며, 놀이시설과 쇼핑시설이 동 평균보다 높습니다. 반면에 음식점과 의료기관은 평균에 비해 다소 적습니다. ",
            "청학동은 교육시설, 놀이시설, 쇼핑시설이 동 평균에 비해 현저히 낮습니다. 요양원, 경로당 등의 노인복지시설이 집중되어 있습니다. ",
            "옥련동은 교육, 노인복지, 놀이, 의료기관이 동 평균에 속합니다. 대형 쇼핑 시설은 1개로 동 평균(4개)에 비해 낮지만 재래시장이 위치하고 있습니다. 또한 청소년 유해 시설이 제일 많이 분포합니다.",
            "연수동은 노인복지시설과 의료기관이 동 평균에 비해 많이 분포되어 있습니다. 청소년 유해시설이 3번째로 많은 동입니다.",
            "선학동은 모든 시설이 동 평균에 비해 적게 분포하고 있습니다. 청소년 유해 시설은 1개로 동평균(18곳)에 비해 적게 분포합니다.")

        //초기화
        binding.songdo.setBackgroundResource(R.drawable.round_btn_blue2)
        binding.songdo.setTextColor(Color.WHITE)
        binding.content.setText(dongStr[0])
        binding.hashTag.setText(tagStr[0])
        binding.graph.setImageResource(img[0])

        /*동네추천화면에서 값 넘어올 경우*/
        if (result.equals("1")){
            //동춘 보이기
            binding.dongchun.setBackgroundResource(R.drawable.round_btn_blue2)
            binding.dongchun.setTextColor(Color.WHITE)
            binding.content.setText(dongStr[1])
            binding.hashTag.setText(tagStr[1])
            binding.graph.setImageResource(img[1])

            //송도 안보이기
            binding.songdo.setBackgroundResource(R.drawable.round_edge_btn_blue)
            binding.songdo.setTextColor(resources.getColor(R.color.colorMain))
        }
        if (result.equals("2")){
            //청학 보이기
            binding.chunghak.setBackgroundResource(R.drawable.round_btn_blue2)
            binding.chunghak.setTextColor(Color.WHITE)
            binding.content.setText(dongStr[2])
            binding.hashTag.setText(tagStr[2])
            binding.graph.setImageResource(img[2])

            //송도 안보이기
            binding.songdo.setBackgroundResource(R.drawable.round_edge_btn_blue)
            binding.songdo.setTextColor(resources.getColor(R.color.colorMain))
        }
        if (result.equals("3")){
            //옥련 보이기
            binding.okryeon.setBackgroundResource(R.drawable.round_btn_blue2)
            binding.okryeon.setTextColor(Color.WHITE)
            binding.content.setText(dongStr[3])
            binding.hashTag.setText(tagStr[3])
            binding.graph.setImageResource(img[3])

            //송도 안보이기
            binding.songdo.setBackgroundResource(R.drawable.round_edge_btn_blue)
            binding.songdo.setTextColor(resources.getColor(R.color.colorMain))
        }
        if (result.equals("4")){
            //청학 보이기
            binding.yeonsoo.setBackgroundResource(R.drawable.round_btn_blue2)
            binding.yeonsoo.setTextColor(Color.WHITE)
            binding.content.setText(dongStr[4])
            binding.hashTag.setText(tagStr[4])
            binding.graph.setImageResource(img[4])

            //송도 안보이기
            binding.songdo.setBackgroundResource(R.drawable.round_edge_btn_blue)
            binding.songdo.setTextColor(resources.getColor(R.color.colorMain))
        }
        if (result.equals("5")){
            //청학 보이기
            binding.sunhak.setBackgroundResource(R.drawable.round_btn_blue2)
            binding.sunhak.setTextColor(Color.WHITE)
            binding.content.setText(dongStr[5])
            binding.hashTag.setText(tagStr[5])
            binding.graph.setImageResource(img[5])

            //송도 안보이기
            binding.songdo.setBackgroundResource(R.drawable.round_edge_btn_blue)
            binding.songdo.setTextColor(resources.getColor(R.color.colorMain))
        }



        for(i in bindingDong.indices) {
            bindingDong[i].setOnClickListener {
                for (j in bindingDong.indices){
                    bindingDong[j].setBackgroundResource(R.drawable.round_edge_btn_blue)
                    bindingDong[j].setTextColor(resources.getColor(R.color.colorMain))
                }
                bindingDong[i].setBackgroundResource(R.drawable.round_btn_blue2)
                bindingDong[i].setTextColor(Color.WHITE)
                binding.content.setText(dongStr[i])
                binding.hashTag.setText(tagStr[i])
                binding.graph.setImageResource(img[i])

            }
        }
    }


}