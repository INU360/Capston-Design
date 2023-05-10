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
