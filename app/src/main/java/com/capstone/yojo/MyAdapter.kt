package com.capstone.yojo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.yojo.databinding.ItemCrimeBinding
import kotlinx.coroutines.InternalCoroutinesApi


class MyAdapter(val binding: MutableList<String>, private val context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val datas = binding


    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyViewHolder(ItemCrimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @OptIn(InternalCoroutinesApi::class)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        //뷰에 데이터 출력
        binding.itemData.text = datas[position]

        //뷰에 이벤트 추가
        binding.itemRoot.setOnClickListener {
            val intent = Intent(context, CrimeSub::class.java)
            intent.putExtra("pos",position)
            context.startActivity(intent)

        }

    }




}
