package com.capstone.yojo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.capstone.yojo.databinding.DongRecommendBinding
import java.lang.Math.abs

class DongRecommend : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 바인딩 선언 */
        val binding = DongRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* '가정유형 버튼' 바인딩  */
        var bindingType = arrayOf(binding.solo,binding.child,binding.senior) //동 버튼 바인딩 배열

        /* '혼자' '아이들' '노인' 버튼 시크바 값  */
        var solo = arrayOf(2,2,1,1,4,4,4,6)
        var child = arrayOf(2,2,4,4,2,3,3,0)
        var senior = arrayOf(4,4,2,2,2,2,2,0)

        /* 동네 카테고리별 점수 담는 변수 : 병원,경로당,학교,어린이집,카페,pc방,코노,단란주점 */
        var songdo = arrayOf(4,4,4,4,4,4,1,0)
        var dongchun = arrayOf(2,2,2,2,4,2,2,3)
        var chunghak = arrayOf(0,1,0,0,1,0,0,2)
        var okryeon = arrayOf(1,2,1,1,2,3,2,4)
        var yeonsu = arrayOf(2,3,2,2,4,4,4,3)
        var sunhak = arrayOf(0,1,0,0,0,1,0,0)

        /* 사용자가 선택한 것과 동네 점수의 차이를 담는 변수 */
        var sub1 = arrayOf(0,0,0,0,0,0,0,0)
        var sub2 = arrayOf(0,0,0,0,0,0,0,0)
        var sub3 = arrayOf(0,0,0,0,0,0,0,0)
        var sub4 = arrayOf(0,0,0,0,0,0,0,0)
        var sub5 = arrayOf(0,0,0,0,0,0,0,0)
        var sub6 = arrayOf(0,0,0,0,0,0,0,0)

        /* 동네 별 점수 결과 변수 : 송도 동춘 청학 옥련 연수 선학 */
        var score = arrayOf(0,0,0,0,0,0,0)

        /* 점수 최소값과 인덱스 담을 변수 */
        var min = 5
        var minIndex = 0

        /* 사용자가 선택한 선호도를 담는 변수 (혼자를 선택하고 사용자가 아무 것도 바꾸지 않을 경우로 초기화) */
        var userChoice = arrayOf(2,2,1,1,4,4,4,6) //병원,경로당,학교,어린이집,카페,pc방,코노,단란주점

        /* seekbar 바인딩 배열 */
        var seeks = arrayOf(binding.seek1,binding.seek2,binding.seek3,binding.seek4,
            binding.seek5,binding.seek6,binding.seek7,binding.seek8)

        /* 초기화 */
        binding.solo.setBackgroundResource(R.drawable.round_btn_blue2)
        binding.solo.setTextColor(Color.WHITE)
        for (i in seeks.indices) {
            seeks[i].setProgress(solo[i],true)
        }


        /* 가정 유형 선택시 이벤트 처리 */
        for(i in bindingType.indices) { //i는 선택된 값
            bindingType[i].setOnClickListener {
                for (j in bindingType.indices){
                    bindingType[j].setBackgroundResource(R.drawable.round_edge_btn_blue)
                    bindingType[j].setTextColor(resources.getColor(R.color.colorMain))
                }
                bindingType[i].setBackgroundResource(R.drawable.round_btn_blue2)
                bindingType[i].setTextColor(Color.WHITE)

                /* 가정유형에 따라 시크바 초기화, i=1 혼자, i=2 아이들, i=3 노인 */
                when (i) {
                    0 -> { //혼자
                        for (j in seeks.indices) {
                            seeks[j].setProgress(solo[j],true) //시크바 변경
                            userChoice[j] = solo[j] //사용자가 선택한 값에 혼자 버튼 누른 경우 시크바 값 대입


                        }
                    }
                    1 -> { //아이들
                        for (j in seeks.indices) {
                            seeks[j].setProgress(child[j],true) //시크바 변경
                            userChoice[j] = child[j] //사용자가 선택한 값에 혼자 버튼 누른 경우 시크바 값 대입
                        }

                    }
                    else -> { //노인
                        for (j in seeks.indices) {
                            seeks[j].setProgress(senior[j],true) //시크바 변경
                            userChoice[j] = senior[j] //사용자가 선택한 값에 혼자 버튼 누른 경우 시크바 값 대입
                        }

                    }
                }//when 끝
            }
        }//for 끝

        for(i in seeks.indices) {
            seeks[i].setOnSeekBarChangeListener( //i는 선택된 값
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar, progress: Int, fromUser: Boolean
                    ) {
                        // write custom code when progress is changed
                        userChoice[i] = progress //사용자가 변경한 시크바 progress를 변수에 넣기


                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        // write custom code when touch is started.
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        // write custom code when touch is stopped

                    }
                })
        }


        /* 검색 버튼 클릭 이벤트 - 화면전환 */
        binding.btnOk.setOnClickListener {

            for(i: Int in 0..7) { //동네 카테고리별 점수와 사용자 선택 선호도 점수의 차(절대값) 대입
                sub1[i] = abs(songdo[i]-userChoice[i])
                sub2[i] = abs(dongchun[i]-userChoice[i])
                sub3[i] = abs(chunghak[i]-userChoice[i])
                sub4[i] = abs(okryeon[i]-userChoice[i])
                sub5[i] = abs(yeonsu[i]-userChoice[i])
                sub6[i] = abs(sunhak[i]-userChoice[i])
            }
            for(i: Int in 0..5) {
                score[0] += sub1[i] // 사용자 선택값과 송도 점수의 카테고리별 점수 차를 누적해서 총점 계산
                score[1] += sub2[i] // 동춘
                score[2] += sub3[i] // 청학
                score[3] += sub4[i]
                score[4] += sub5[i]
                score[5] += sub6[i]
            }
            for(i: Int in 0..5) {
                if(score[i]<=min){
                    min = score[i]
                    minIndex = i
                }
            }

            var strIndex:String = minIndex.toString()
            val intent = Intent(this, RecommendSub::class.java)
            intent.putExtra("index",strIndex)
            startActivity(intent)
        }



    }


}