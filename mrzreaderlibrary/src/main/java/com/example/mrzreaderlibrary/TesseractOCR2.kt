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

    private val TAG                                 = "TextRecognitionHelper"

    private val DATA_PATH                    = Environment.getExternalStorageDirectory().toString() + "/AndroidOCR/"

    private var tesseractApi: TessBaseAPI            = TessBaseAPI()

    init {
        writeFile()
        initTesseract()
    }

    /**
     * Initialize tesseract engine.
     *
     * @param language Language code in ISO-639-3 format.
     */

    private fun writeFile(){
        val paths = arrayOf(DATA_PATH, DATA_PATH + "tessdata/")

        for (path in paths) {
            val dir = File(path)
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory $path on sdcard failed")
                    return
                } else {
                    Log.v(TAG, "Created directory $path on sdcard")
                }
            }
        }

        val file = File(DATA_PATH + "tessdata/" + language + ".traineddata")

        if(file.exists())
            file.delete()

        if (!file.exists()) {
            try {
                val input       = context.assets.open("$language.traineddata")

                val output      = FileOutputStream(File(DATA_PATH + "tessdata/", language + ".traineddata"))

                val buf = ByteArray(1024)
                var len = input.read(buf)

                while (len != -1) {
                    output.write(buf, 0, len)
                    len = input.read(buf)
                }

                input.close()
                output.close()

                Log.v(TAG, "Copied $language traineddata")
            } catch (e: IOException) {
                Log.e(TAG, "Was unable to copy $language traineddata $e")
            }

        }
    }

    private fun initTesseract() {
        try {
            tesseractApi.init(DATA_PATH, language)
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