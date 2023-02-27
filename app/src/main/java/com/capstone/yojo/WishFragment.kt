package com.capstone.yojo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.capstone.yojo.databinding.WishListSheetBinding

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class WishFragment : Fragment()  {
    private lateinit var wishSheetbinding: WishListSheetBinding
    lateinit var wishItemList : ArrayList<ListItemData>

    private lateinit var emptyTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        wishSheetbinding = WishListSheetBinding.inflate(layoutInflater)

        wishItemList = ArrayList()

        emptyTextView = wishSheetbinding.emptyTextView

        // 리스트뷰 내용 어댑터 이용 주르륵 출력
        val adapter = WishListAdapter(requireContext(), wishItemList)
        wishSheetbinding.wishListView.adapter = adapter

        val database = Firebase.database
        val uid = context?.let { getUidFromSharedPrefs(it) }
        if (uid != null) {

            val wishItemsRef = database.getReference("WishListItems").child(uid)

            wishItemsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    wishItemList.clear()
                    for (ds in snapshot.children) { // 건물명들

                        for(wish in ds.children) { // 건물명 하위 자식들
                            val name = wish.child("name").value as String
                            val address = wish.child("address").value as String
                            val latitude = wish.child("latitude").value as Double
                            val longitude = wish.child("longitude").value as Double
                            val item = wish.child("item").value as String
                            val imgIndex = wish.child("imgIndex").value as Long
                            val position = wish.child("position").value as Long
                            val keyValue = wish.child("keyValue").value as String

                            val set = ListItemData(item, imgIndex.toInt(), name, address, latitude, longitude, position, keyValue)

                            wishItemList.add(set)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    updateVisibility()

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                }
            })
        }



        return wishSheetbinding.root
    }



    private fun getUidFromSharedPrefs(context: Context): String? {
        val prefs = context.getSharedPreferences("MyUID", Context.MODE_PRIVATE)
        return prefs.getString("uid", null)
    }

    private fun updateVisibility() {
        if (wishItemList.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            wishSheetbinding.wishListView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            wishSheetbinding.wishListView.visibility = View.VISIBLE
        }
    }


}