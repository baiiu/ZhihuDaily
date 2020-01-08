package com.baiiu.gank

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/**
 * author: zhuzhe
 * time: 2020-01-08
 * description:
 */
class SimpleModel : ViewModel() {

    val strLiveData = MutableLiveData<String>()

    var str: Int = 0

    val mHandler: Handler = Handler()


    private val mRunnable: Runnable = object : Runnable {
        override fun run() {
            ++str

            strLiveData.postValue(str.toString())

            mHandler.postDelayed(this, 1000)
        }
    }

    fun start() {
        mHandler.post(mRunnable)
    }


    override fun onCleared() {
        str = 0
    }

}