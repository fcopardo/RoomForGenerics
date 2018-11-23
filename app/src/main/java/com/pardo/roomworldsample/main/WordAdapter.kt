package com.pardo.roomworldsample.main

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import com.pardo.roomwithaword.entities.Word
import com.pardo.roomworldsample.ui.GenericViewHolder

class WordAdapter : RecyclerView.Adapter<GenericViewHolder<CellUI, Word>>() {

    private var elements : ArrayList<Word> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GenericViewHolder<CellUI, Word> {
        val ui = CellUI(p0.context)
        val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        ui.layoutParams = lp
        ui.setData(elements[p1])
        return GenericViewHolder(ui)
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    override fun onBindViewHolder(p0: GenericViewHolder<CellUI, Word>, p1: Int) {
        p0.resetData(elements[p1])
    }

    fun setElements(list : List<Word>){
        Log.e("Room", "reset adapter :"+ elements.size+" - "+list.size)
        elements.clear()
        elements.addAll(list)
        notifyDataSetChanged()
    }
}