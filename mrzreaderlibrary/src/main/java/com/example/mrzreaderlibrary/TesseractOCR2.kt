package com.example.mrzreaderlibrary

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class TesseractOCR2(val context: Context, val language: String) {

    private val TAG                             = "TextRecognitionHelper"
    private val TESSERACT_TRAINED_DATA_FOLDER   = "tessdata"
    private val TESSERACT_PATH                  = Environment.getExternalStorageDirectory().absolutePath + "/tesseract/"


    private var tesseractApi: TessBaseAPI = TessBaseAPI()

    init {
        prepareTesseract()
    }

    /**
     * Initialize tesseract engine.
     *
     * @param language Language code in ISO-639-3 format.
     */
    private fun prepareTesseract() {
        try {
            prepareDirectory(TESSERACT_PATH + TESSERACT_TRAINED_DATA_FOLDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        copyTessDataFiles(TESSERACT_TRAINED_DATA_FOLDER)
        initTesseract()
    }

    private fun prepareDirectory(path: String) {

        val dir = File(path)
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(
                    TAG,
                    "ERROR: Creation of directory $path failed, check does Android Manifest have permission to write to external storage."
                )
            }
        } else {
            Log.i(TAG, "Created directory $path")
        }
    }

    private fun copyTessDataFiles(path: String) {
        try {
            val fileList = context.assets.list("")

            if(fileList != null) {
                for (fileName in fileList) {
                    val pathToDataFile = "$TESSERACT_PATH$path/$fileName"
                    if (!File(pathToDataFile).exists() && fileName.startsWith("en")) {
                        val input = context.assets.open(fileName)
                        val out = FileOutputStream(pathToDataFile)
                        val buf = ByteArray(1024)
                        var length = input.read(buf)

                        while (length > 0) {
                            out.write(buf, 0, length)

                            length = input.read(buf)
                        }

                        input.close()
                        out.close()
                        Log.d(TAG, "Copied " + fileName + "to tessdata")
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Unable to copy files to tessdata " + e.message)
        }

    }

    private fun initTesseract() {
        try {
            tesseractApi.init(TESSERACT_PATH, language)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun getOCRResult(bitmap: Bitmap): String? {
        tesseractApi.setImage(bitmap)
        return tesseractApi.utF8Text
    }

    fun endTess() {
        tesseractApi.end()
    }
}