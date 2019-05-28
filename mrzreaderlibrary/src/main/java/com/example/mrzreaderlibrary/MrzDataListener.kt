package com.example.mrzreaderlibrary

import com.example.mrz.MrzRecord


interface MrzDataListener {
    fun onMrzDataRead(mrzParser: MrzRecord)
}