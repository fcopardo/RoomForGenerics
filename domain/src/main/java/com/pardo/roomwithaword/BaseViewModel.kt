package com.pardo.roomwithaword

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

class BaseViewModel<T>(application: Application, aClass : Class<T>) : AndroidViewModel(application) {

    init{

    }

}