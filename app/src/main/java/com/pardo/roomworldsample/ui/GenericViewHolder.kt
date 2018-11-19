package com.pardo.roomworldsample.ui

import android.support.v7.widget.RecyclerView
import android.view.View

class GenericViewHolder<T : UI<X>, X> : RecyclerView.ViewHolder {

    constructor(view : T) : super(view as View)

    fun getView() : T {
        return this.itemView as T
    }

    fun resetData(data : X){
        getView().setData(data)
    }
}