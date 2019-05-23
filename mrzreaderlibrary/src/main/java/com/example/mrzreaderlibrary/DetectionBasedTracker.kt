package com.example.mrzreaderlibrary

object DetectionBasedTracker {
    fun detectPassportZone(frame: Long): ByteArray? =
        detectMrzRegion(frame)

    private external fun detectMrzRegion(frame: Long): ByteArray
}
