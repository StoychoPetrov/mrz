package com.example.mrzreaderlibrary

import android.content.Context
import android.graphics.Bitmap
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class TesseractOCR2(val context: Context, val language: String) {

    private var tesseractApi: TessBaseAPI = TessBaseAPI()

    init {
        saveFileFromRowInStorage()
    }

    private fun saveFileFromRowInStorage(){
        val fileName                = "en.traineddata"
        val dstPathDir: String      = context.filesDir.path.plus("/tesseract/tessdata/")
        val dstFilePath: String     = dstPathDir.plus(fileName)

        val inputStream: InputStream = context.resources.openRawResource(
                context.resources.getIdentifier("en",
                        "raw", context.packageName))

        val file      = File(dstPathDir)

        if (file.exists() || File(dstPathDir).mkdirs()){
            inputStream.close()
            initTesseract()
        }
        else {
            val outputStream  = FileOutputStream(File(dstFilePath))

            val buf     = ByteArray(1024)
            var bytes   = inputStream.read(buf)

            while (bytes != -1) {
                outputStream.write(buf)
                bytes = inputStream.read(buf)
            }

            inputStream.close()
            outputStream.close()
            initTesseract()
        }

    }

    private fun initTesseract() {
        try {
            tesseractApi.init(context.getFilesDir().path.plus( "/tesseract/"), language)
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