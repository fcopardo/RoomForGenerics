package com.pardo.roomworldsample.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.pardo.roomwithaword.entities.Word
import com.pardo.roomworldsample.R
import com.pardo.roomworldsample.ui.UI

class CellUI : LinearLayout, UI<Word> {

    private lateinit var data : Word
    private lateinit var text : TextView

    override fun setData(data: Word) {
        this.data = data
        text.text = data.getWord()
    }

    override fun asView(): View {
        return this
    }

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
        val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var content = inflater.inflate(R.layout.recyclerview_item, this)
        text = findViewById(R.id.textView)
    }
}