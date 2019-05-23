package com.example.mrzreaderlibrary

import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class JavaCamera2Frame(private val yuvFrameData: Mat, private val uvFrameData: Mat, private val width: Int, private val height: Int): CameraBridgeViewBase.CvCameraViewFrame {

    private val rgba = Mat()

    override fun rgba(): Mat {
        Imgproc.cvtColorTwoPlane(yuvFrameData, uvFrameData, rgba, Imgproc.COLOR_YUV2RGBA_NV21)

        return rgba
    }

    override fun gray(): Mat = yuvFrameData.submat(0, height, 0, width)

    fun release() { rgba.release() }
}