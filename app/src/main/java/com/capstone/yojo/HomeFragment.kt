package com.capstone.yojo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.yojo.databinding.HomeFragmentBinding


class HomeFragment : Fragment() {
    private lateinit var binding: HomeFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)


        /* 바인딩 선언 */
        //val binding = HomeFragmentBinding.inflate(layoutInflater)

        //binding.tabs.bringToFront()

        /* 아파트, 오피스텔 중에 어떤 버튼을 클릭했는지 담는 변수 선언 */
        var houseType = ""

        /* 아파트 버튼 클릭 이벤트 - 화면전환 */

        binding.btnApt.setOnClickListener {
            houseType = "아파트" //아파트 버튼을 클릭할 경우 0
            val intent = Intent(activity, HouseMap::class.java)
            intent.putExtra("houseType", houseType)
            startActivity(intent)
        }


        /* 오피스텔 버튼 클릭 이벤트 - 화면전환 */
        binding.btnOpi.setOnClickListener {
            houseType = "오피스텔" //오피스텔 버튼을 클릭할 경우 1
            val intent = Intent(activity, HouseMap::class.java)
            intent.putExtra("houseType", houseType)

            startActivity(intent)
        }

        /* 장소 버튼 클릭 이벤트 - 화면전환 */
        binding.btnPlace.setOnClickListener {
            val intent = Intent(activity, PlaceMap::class.java)
            startActivity(intent)
        }

        /* 동 추천 버튼 클릭 이벤트 - 화면전환 */
        binding.btnRecommend.setOnClickListener {
            val intent = Intent(activity, DongRecommend::class.java)
            startActivity(intent)
        }

        /* 동별 비교 버튼 클릭 이벤트 - 화면전환 */
        binding.btnComp.setOnClickListener {
            val intent = Intent(activity, DongCompare::class.java)
            startActivity(intent)
        }

        /* 치안 버튼 클릭 이벤트 - 화면전환 */
        binding.btnCrime.setOnClickListener {
            val intent = Intent(activity, CrimeMain::class.java)
            startActivity(intent)
        }
        return binding.root
    }

}



