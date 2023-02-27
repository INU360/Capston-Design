package com.capstone.yojo
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class WishListAdapter(private val context: Context, private val itemList: ArrayList<ListItemData>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val database = Firebase.database

    private class ViewHolder(v: View?) {
        val nameTextView: TextView = v?.findViewById(R.id.name_wish) as TextView
        val addressTextView: TextView = v?.findViewById(R.id.address_wish) as TextView
        val detailTextView: TextView = v?.findViewById(R.id.detail_wish) as TextView

        val infoView : LinearLayout = v?.findViewById(R.id.body_wish) as LinearLayout
        val deleteButton: ImageView = v?.findViewById(R.id.delete_wish) as ImageView
    }

    @SuppressLint("SuspiciousIndentation")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.wish_list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = itemList[position]

        // 위시리스트 내 삭제버튼 위해 선언
        val wishPosition = item.position
        val wishKeyValue = item.keyValue

        viewHolder.nameTextView.text = item.name
        viewHolder.addressTextView.text = item.address
        viewHolder.detailTextView.text = item.item

        // 위시리스트 목록 OnClickListener
        viewHolder.infoView.setOnClickListener {
            // name, address, latitude, longitude
            // 지도 띄워서 마커 + 카드뷰

            val intent = Intent(context, WishMapActivity::class.java)
            intent.putExtra("name", item.name)
            intent.putExtra("address", item.address)
            intent.putExtra("latitude", item.latitude)
            intent.putExtra("longitude", item.longitude)


            context.startActivity(intent)
        }

        // 삭제버튼 OnClickListener
        viewHolder.deleteButton.setOnClickListener {
            val uid = getUidFromSharedPrefs(context) // UID 가져옴
            if (uid != null) {
                val listItemsRef = database.getReference("WishListItems").child(uid).child(wishKeyValue!!)

                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()

                    listItemsRef.child(wishPosition.toString()).removeValue()
                // WishListItems - uid - 건물명 - 인덱스번호
                // 삭제위해 ListViewAdapter 에서 찜하기 했을때의 포지션값 (기존 셋팅이 0으로 되어있으면 필요없을듯)
                // 그냥 파베 값 바로 삭제하게 하는게 나아보임
                // 인텐트 했을때 층수를 받아와서 일치하면 삭제 (야매)
                // 아니면 info 정보가 일치하면 삭제 식으로 구현하기

            }
        }

        return view
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun getUidFromSharedPrefs(context: Context): String? {
        val prefs = context.getSharedPreferences("MyUID", Context.MODE_PRIVATE)
        return prefs.getString("uid", null)
    }

}
