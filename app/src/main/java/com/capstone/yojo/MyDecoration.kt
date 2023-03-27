package com.capstone.yojo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

//리사이클러뷰 꾸미는 클래스
class MyDecoration(private val context: Context):RecyclerView.ItemDecoration(){
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State){
        super.onDraw(c, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)


    }

    @SuppressLint("ResourceAsColor")
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view) + 1

        if(index % 3 == 0)
            outRect.set(10,10,10,60)
        else
            outRect.set(10,10,10,0)
        view.setBackgroundResource(R.drawable.round_edge_btn)
        ViewCompat.setElevation(view, 20.0f)
    }
}