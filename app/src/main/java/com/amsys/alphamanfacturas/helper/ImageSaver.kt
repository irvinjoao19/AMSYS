package com.amsys.alphamanfacturas.helper

import android.content.Context
import android.media.Image
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

internal class ImageSaver(
        /**
         * The JPEG image
         */
        private val image: Image,
        /**
         * The file we save the image into.
         */
        private val file: File,
        /**
         * Fecha Asignacion only Lecturas
         */
        private val fechaAsignacion: String,
        private val direccion: String,
        private val distrito: String,
        private val context: Context
) : Runnable {

    override fun run() {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        var output: FileOutputStream? = null
        try {
            output = FileOutputStream(file).apply {
                write(bytes)
            }
            Util.getAngleImage(context, file.absolutePath, fechaAsignacion, direccion, distrito)
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        } finally {
            image.close()
            output?.let {
                try {
                    it.close()
                } catch (e: IOException) {
                    Log.e(TAG, e.toString())
                }
            }
        }
    }

    companion object {
        /**
         * Tag for the [Log].
         */
        private val TAG = "ImageSaver"
    }
}