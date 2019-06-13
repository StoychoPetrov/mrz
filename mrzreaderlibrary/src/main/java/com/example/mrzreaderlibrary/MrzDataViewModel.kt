package com.example.mrzreaderlibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mrz.MrzRecord

class MrzDataViewModel : ViewModel() {

    var mrzRecord = MutableLiveData<MrzRecord>()

    fun getMrzRecord() : LiveData<MrzRecord?> = mrzRecord

    fun setMrzRecord(mrzData: MrzRecord) {
        mrzRecord.value = mrzData
    }
}