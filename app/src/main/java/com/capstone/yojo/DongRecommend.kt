package com.capstone.yojo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.yojo.databinding.DongRecommendBinding

class DongRecommend : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* 바인딩 선언 */
        val binding = DongRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}