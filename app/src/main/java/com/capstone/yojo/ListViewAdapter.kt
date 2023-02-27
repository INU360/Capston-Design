package com.capstone.yojo
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.capstone.yojo.databinding.BottomListItemListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


// 리스트 안에 있는거 터치시 토스트 창 띄워주는 어댑터 코드
class ListViewAdapter (val context : Context,
                       // 매물 정보
                       val list : ArrayList<String>,
                       // keyValue (건물명)을  key로 가지고 이름,주소,위도,경도 value.
                       val infoMap : MutableMap<String, MutableList<SaveWishData>>,
                       val keyValue : String?,
                       val bottomSheetDialog: BottomSheetDialog) : BaseAdapter()
{
    private lateinit var bListBinding : BottomListItemListBinding

    private val database = Firebase.database

    private val listItemDataMap = mutableMapOf<Int, ListItemData>()


    init {
        val uid = getUidFromSharedPrefs(context)
        if (uid != null) {
            val listItemsRef = database.getReference("WishListItems").child(uid).child(keyValue!!)
            listItemsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listItemDataMap.clear()
                        for (childSnapshot in dataSnapshot.children) {
                            val position = childSnapshot.key?.toInt() ?: 0
                            val listItemData = childSnapshot.getValue(ListItemData::class.java)
                            listItemData?.let {
                                // Map에 ListItemData 넣음
                                listItemDataMap[position] = it
                            }
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
        // p1 = convertView  p2 = parent

        // ListViewAdapter 클래스는 bottom_list_item_list.xml 을 바인딩해서 가져옴
        bListBinding = BottomListItemListBinding.inflate(LayoutInflater.from(context))


        /*
        val view : View
        val holder : ViewHolder

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.bottom_list_item_list, null)
            holder = ViewHolder()
            holder.itemsText = view.findViewById<TextView>(R.id.textView)
            holder.wishBtnView = view.findViewById<ImageView>(R.id.wish_btn)
            view.tag = holder
        }
        else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }
         */

        val itemsText = bListBinding.textView
        val item = list[position]
        itemsText.setText(item)

        val infoMap = infoMap[keyValue]
        var name : String? = null
        var address : String? = null
        var latitude : Double? = null
        var longitude : Double? = null

        for(i in infoMap!!){
            name = i.name
            address = i.address
            latitude = i.latitude
            longitude = i.longitude
        }

        val myPosition = position.toLong()

        val wishBtnView = bListBinding.wishBtn
        val listItemData = listItemDataMap[position]
        if (listItemData != null) {
            // 해당 position 에 ListItemData 존재하면 꽉찬하트
            wishBtnView.setImageResource(R.drawable.heart_fill)
        } else {
            // 없으면 빈하트
            wishBtnView.setImageResource(R.drawable.heart_wish)
        }

        wishBtnView.setOnClickListener {
            val uid = getUidFromSharedPrefs(context) // UID 가져옴
            if (uid != null) {
                val listItemsRef = database.getReference("WishListItems").child(uid).child(keyValue!!)
                val newData = ListItemData(item, 1, name, address, latitude, longitude, myPosition, keyValue)
                val imgIndex = listItemData?.imgIndex ?: 0
                if (imgIndex == 0) {
                    Toast.makeText(context, "찜했습니다", Toast.LENGTH_SHORT).show()
                    wishBtnView.setImageResource(R.drawable.heart_fill)
                    listItemsRef.child(position.toString()).setValue(newData)
                } else {
                    Toast.makeText(context, "찜 취소", Toast.LENGTH_SHORT).show()
                    wishBtnView.setImageResource(R.drawable.heart_wish)
                    listItemsRef.child(position.toString()).removeValue()
                }
            }
        }

        return bListBinding.root
    }

    override fun getCount(): Int { return list.size }

    override fun getItem(position: Int): Any { return list[position] }

    override fun getItemId(position: Int): Long { return position.toLong() }


    private fun getUidFromSharedPrefs(context: Context): String? {
        val prefs = context.getSharedPreferences("MyUID", Context.MODE_PRIVATE)
        return prefs.getString("uid", null)
    }

}
