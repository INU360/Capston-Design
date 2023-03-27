package com.capstone.yojo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.yojo.databinding.HouseFilterBinding

class HouseFilter : AppCompatActivity() {

    var resultPriceStart = ""
    var resultPriceFinish = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 바인딩 선언 */
        val binding = HouseFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* '거래 유형 버튼(매매,전세,월세)' 이벤트 처리  */
        var bindingType =
            arrayOf(binding.maemae, binding.junsae, binding.wolsae) //거래 유형 버튼(매매,전세,월세) 바인딩 배열
        var resultType = "" //선택된 거래 유형(매매,전세,월세)을 담을 변수 선언
        var typeStr = arrayOf("매매", "전세", "월세")
        for (i in bindingType.indices) { //버튼의 수(매매,전세,월세) 만큼 for문 반복
            bindingType[i].setOnClickListener { //변수 i:사용자가 클릭한 버튼의 인덱스를 담음(매매:0,전세:1,월세:2)
                for (j in bindingType.indices) { //모든 버튼은 기본색상인 파란색으로 변경
                    bindingType[j].setBackgroundResource(R.drawable.round_btn_blue)
                }
                bindingType[i].setBackgroundResource(R.drawable.round_btn_yellow) //사용자가 선택한 버튼은 노란색으로 변경
                resultType = typeStr[i] //선택된 거래 유형(매매,전세,월세)을 담음

                if (i == 0) {
                    binding.tvMainName2.text = "가격"
                } else {
                    binding.tvMainName2.text = "보증금"
                }
            }
        }

        /* '면적 버튼' 이벤트 처리  */
        var bindingArea = arrayOf(
            binding.btnArea1, binding.btnArea2, binding.btnArea3, binding.btnArea4,
            binding.btnArea5, binding.btnArea6
        ) //면적 버튼 바인딩 배열
        var resultArea = "" //선택된 면적을 담을 변수
        var areaStr = arrayOf("전체", "10평대", "20평대", "30평대", "40평대", "50평대")
        for (i in bindingArea.indices) {
            bindingArea[i].setOnClickListener {
                for (j in bindingArea.indices) {
                    bindingArea[j].setBackgroundResource(R.drawable.round_btn_blue)
                }
                bindingArea[i].setBackgroundResource(R.drawable.round_btn_yellow)
                resultArea = areaStr[i] //선택된 면적을 담음
            }
        }

        /* '준공 년도 버튼' 이벤트 처리  */
        var bindingYear = arrayOf(
            binding.btnYear1,
            binding.btnYear2,
            binding.btnYear3,
            binding.btnYear4
        ) //연도 버튼 바인딩 배열
        var resultYear = "" //선택된 연도 (사용자가 선택 안한 경우:-1)
        var yearStr = arrayOf("전체", "5년이내", "10년이내", "20년이내")
        for (i in bindingYear.indices) {
            bindingYear[i].setOnClickListener {
                for (j in bindingYear.indices) {
                    bindingYear[j].setBackgroundResource(R.drawable.round_btn_blue)
                }
                bindingYear[i].setBackgroundResource(R.drawable.round_btn_yellow)
                resultYear = yearStr[i]//선택된 면적 버튼 인덱스 대입
            }
        }

        /* '적용 버튼' 클릭 이벤트 - 화면 전환(양방향 전달) */
        binding.btnOk.setOnClickListener {
            resultPriceStart = binding.editStart.text.toString() //사용자가 입력한 최소 가격
            resultPriceFinish = binding.editFinish.text.toString() //사용자가 입력한 최대 가격
            if (resultType == "" || resultArea == "" || resultYear == "") {
                Toast.makeText(this, "모두 선택해 주세요.", Toast.LENGTH_SHORT).show()
            } else { //사용자가 선택한 조건들을 HouseFilter로 값 넘김
                intent.putExtra("resultType", resultType)
                intent.putExtra("resultArea", resultArea)
                intent.putExtra("resultYear", resultYear)
                intent.putExtra("resultPriceStart", resultPriceStart)
                intent.putExtra("resultPriceFinish", resultPriceFinish)
                setResult(RESULT_OK, intent)

                finish()
            }
        }
    }
}

