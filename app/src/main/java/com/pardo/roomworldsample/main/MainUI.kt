package com.pardo.roomworldsample.main

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.pardo.roomwithaword.entities.Word
import com.pardo.roomworldsample.ui.UI

class MainUI : FrameLayout, UI<List<Word>> {

    override fun setData(data: List<Word>) {
        Log.e("RoomDB", "called MainUI setData")
        adapter.setElements(data)
    }

    override fun asView(): View {
        return this
    }

    private lateinit var mainContent : RecyclerView
    private lateinit var adapter : WordAdapter

    constructor(context : Context) : super(context){
        init()
    }
    constructor(context : Context, attr : AttributeSet) : super(context, attr){
        init()
    }
    constructor(context : Context, attr : AttributeSet, defStyleAttr : Int) : super(context, attr, defStyleAttr){
        init()
    }

    private fun init(){
        mainContent = RecyclerView(context)
        mainContent.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(mainContent)
        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mainContent.layoutManager = layoutManager
        adapter = WordAdapter()
        mainContent.adapter = adapter
    }

}