package com.capstone.yojo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.capstone.yojo.databinding.BottomListItemListBinding
import com.capstone.yojo.model.MarkerData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


// 리스트 안에 있는거 터치시 토스트 창 띄워주는 어댑터 코드
class ListViewAdapter(
    val context: Context,
    val data: MarkerData,
) : BaseAdapter() {
    private val database = Firebase.database
    private val listItemDataMap = mutableListOf<ListItemData>()

    init {
        val uid = getUidFromSharedPrefs(context)
        if (uid != null) {
            val listItemsRef = database.getReference("WishListItems").child(uid).child(data.key)
            listItemsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listItemDataMap.clear()

                    for (childSnapshot in dataSnapshot.children) {
                        val listItemData =
                            childSnapshot.getValue(ListItemData::class.java) ?: continue
                        listItemDataMap.add(listItemData)
                    }

                    notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // handle error
                }
            })
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // ListViewAdapter 클래스는 bottom_list_item_list.xml 을 바인딩해서 가져옴
        val binding =
            if (convertView == null) BottomListItemListBinding.inflate(LayoutInflater.from(context), parent, false)
            else BottomListItemListBinding.bind(convertView)

        with(binding) {
            val detail = data.details[position]

            val square = "\u33a1" // 제곱미터 특수문자 사용
            val enter = "\n" // 줄바꿈
            val item = if (detail.price != 0L) {
                " ${detail.type} / ${detail.size} $square / ${detail.floor} 층 $enter ${detail.price} 만원 $enter 계약년월 : ${detail.date} "

            } else if (detail.monthly == 0L) {
                " ${detail.type} / ${detail.size} $square / ${detail.floor} 층 $enter ${detail.deposit} 만원 $enter 계약년월 : ${detail.date} "

            } else {
                " ${detail.type} / ${detail.size} $square / ${detail.floor} 층 $enter ${detail.deposit} / ${detail.monthly} $enter 계약년월 : ${detail.date}"
            }

            textView.text = item

            if (listItemDataMap.any { it.position.toInt() == detail.key }) {
                wishBtn.setImageResource(R.drawable.heart_fill)
            } else {
                wishBtn.setImageResource(R.drawable.heart_wish)
            }

            wishBtn.setOnClickListener {
                val uid = getUidFromSharedPrefs(context) ?: return@setOnClickListener // UID 가져옴

                val listItemsRef =
                    database.getReference("WishListItems").child(uid).child(data.key)
                val newData =
                    ListItemData(
                        item,
                        1,
                        data.name,
                        data.address,
                        data.latitude,
                        data.longitude,
                        detail.key.toLong(),
                        data.key
                    )

                if (listItemDataMap.any { it.position == detail.key.toLong() }) {
                    Toast.makeText(context, "찜 취소", Toast.LENGTH_SHORT).show()
                    wishBtn.setImageResource(R.drawable.heart_wish)
                    listItemsRef.child(detail.key.toString()).removeValue()

                } else {
                    Toast.makeText(context, "찜했습니다", Toast.LENGTH_SHORT).show()
                    wishBtn.setImageResource(R.drawable.heart_fill)
                    listItemsRef.child(detail.key.toString()).setValue(newData)
                }
            }
        }

        return binding.root
    }

    override fun getCount(): Int {
        return data.details.size
    }

    override fun getItem(position: Int): Any {
        return data.details[position]
    }

    override fun getItemId(position: Int): Long {
        return data.details[position].key.toLong()
    }

    private fun getUidFromSharedPrefs(context: Context): String? {
        val prefs = context.getSharedPreferences("MyUID", Context.MODE_PRIVATE)
        return prefs.getString("uid", null)
    }
}
