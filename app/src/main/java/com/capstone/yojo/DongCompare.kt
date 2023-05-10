package com.capstone.yojo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstone.yojo.databinding.DongCompareBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class DongCompare : AppCompatActivity() {

    private val binding by lazy { DongCompareBinding.inflate(layoutInflater) }

    /**
     * 버튼 리스트
     */
    private val bindingDong by lazy {
        listOf(
            binding.songdo, binding.dongchun, binding.chunghak, binding.okryeon,
            binding.yeonsoo, binding.sunhak
        ) //동 버튼 바인딩 배열
    }

    /**
     * 그래프 데이터
     * (송도, 동춘, 청학, 옥련, 연수, 선학동 순)
     */
    private val facilities = listOf(
        Facility(140, 0, 28, 50, 4, 11, 6, 2, 12, 3, 178, 46, 21, 49, 97),
        Facility(46, 0, 13, 26, 6, 7, 3, 2, 5, 1, 64, 17, 3, 21, 22),
        Facility(9, 0, 4, 16, 18, 1, 0, 1, 1, 1, 22, 11, 1, 14, 19),
        Facility(31, 0, 8, 24, 6, 8, 2, 0, 1, 0, 50, 10, 3, 18, 20),
        Facility(41, 3, 13, 29, 8, 15, 4, 0, 0, 0, 68, 23, 4, 30, 37),
        Facility(8, 0, 2, 13, 10, 3, 0, 0, 0, 0, 15, 6, 2, 10, 13),
    )
/**
     * 태그
     */
    private val tags = listOf(
        "#자녀 가정 추천  #노인 가정 추천  #1인 가구 추천",
        "#대학생 추천  #1인 가구 추천 #자녀 가정 비추천",
        "#노인 가정 추천",
        "#자녀 가정 비추천",
        "#노인 가정 추천  #자녀 가정 비추천",
        "#모든 가정 추천"
    )

    /**
     * 설명
     */
    private val descriptions = listOf(
        "송도동은 교육시설, 노인복지시설, 놀이시설, 쇼핑, 음식점, 의료기관이 모두 다른 동에 비해 많이 위치하고 있습니다. 특히 교육기관, 음식점, 의료기관이 송도동에 많이 집중되어 있습니다.",
        "동춘동의 교육시설과 노인복지시설은 동 평균에 속하며, 놀이시설과 쇼핑시설이 동 평균보다 높습니다. 반면에 음식점과 의료기관은 평균에 비해 다소 적습니다. ",
        "청학동은 교육시설, 놀이시설, 쇼핑시설이 동 평균에 비해 현저히 낮습니다. 요양원, 경로당 등의 노인복지시설이 집중되어 있습니다. ",
        "옥련동은 교육, 노인복지, 놀이, 의료기관이 동 평균에 속합니다. 대형 쇼핑 시설은 1개로 동 평균(4개)에 비해 낮지만 재래시장이 위치하고 있습니다. 또한 청소년 유해 시설이 제일 많이 분포합니다.",
        "연수동은 노인복지시설과 의료기관이 동 평균에 비해 많이 분포되어 있습니다. 청소년 유해시설이 3번째로 많은 동입니다.",
        "선학동은 모든 시설이 동 평균에 비해 적게 분포하고 있습니다. 청소년 유해 시설은 1개로 동평균(18곳)에 비해 적게 분포합니다."
    )
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 동네추천 화면에서 추천받은 동네 받아오기
        val result = intent.getStringExtra("result")

        // 바인딩 선언
        setContentView(binding.root)

        initUi(result?.toIntOrNull() ?: 0)
    }

    /**
     * 뷰 초기화
     */
    private fun initUi(initIndex: Int) {
        bindingDong.forEach {
            it.setOnClickListener { view ->
                bindingDong.forEachIndexed { index, button ->
                    if (button == view) {
                        button.setBackgroundResource(R.drawable.round_btn_blue2)
                        button.setTextColor(Color.WHITE)
                        binding.content.text = descriptions[index]
                        binding.hashTag.text = tags[index]
                        loadChart(index)

                    } else {
                        button.setBackgroundResource(R.drawable.round_edge_btn_blue)
                        button.setTextColor(resources.getColor(R.color.colorMain))
                    }
                }
            }
        }
           // 차트 초기화
        with(binding.graph) {
            description.isEnabled = false
            extraBottomOffset = 10f

            // 범례
            with(legend) {
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }

            // x 축
            with(xAxis) {
                axisMinimum = 0f
                axisMaximum = 6f
                granularity = 1f
                textColor = ContextCompat.getColor(this@DongCompare, R.color.colorMain)
                textSize = 10f
                setCenterAxisLabels(true)
                setDrawGridLines(false)
                setDrawAxisLine(false)
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return when (value.toInt()) {
                            0 -> "교육"
                            1 -> "노인복지"
                            2 -> "놀이시설"
                            3 -> "쇼핑"
                            4 -> "편의시설"
                            else -> "의료시설"
                        }
                    }
                }
            }

            with(axisLeft) {
                axisMinimum = 0f // this replaces setStartAtZero(true)
                granularity = 0.1f
                textSize = 10f
                setDrawGridLines(true)
                setDrawZeroLine(false)
                setDrawAxisLine(false)
            }

            axisRight.isEnabled = false
        }

        // performClick 함수 호출시, 해당 뷰에 설정된 OnClickListener 가 호출됨
        bindingDong.getOrNull(initIndex)?.performClick()
    }

        
