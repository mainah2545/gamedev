package com.kanyarat.gamedev

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.LruCache
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class LoadTask {
    @Volatile
    var cancelled = false
        private set

    fun cancel() {
        cancelled = true
    }
}

object RemoteLoader {
    private const val TIMEOUT_MS = 5000

    private val executor = Executors.newFixedThreadPool(8)
    private val mainHandler = Handler(Looper.getMainLooper())

    private val imageCache = object : LruCache<String, Bitmap>(
        (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()
    ) {
        override fun sizeOf(key: String, value: Bitmap): Int = value.byteCount / 1024
    }

    private val textCache = LruCache<String, String>(64)

    fun loadImage(url: String, maxSize: Int, onResult: (Result<Bitmap>) -> Unit): LoadTask {
        val task = LoadTask()
        val key = "$url@$maxSize"
        val cached = imageCache.get(key)
        if (cached != null) {
            onResult(Result.success(cached))
            return task
        }
        executor.execute {
            if (task.cancelled) return@execute
            val result = runCatching { decode(fetch(url), maxSize) }
            result.getOrNull()?.let { imageCache.put(key, it) }
            deliver(task, result, onResult)
        }
        return task
    }

    fun loadText(url: String, onResult: (Result<String>) -> Unit): LoadTask {
        val task = LoadTask()
        val cached = textCache.get(url)
        if (cached != null) {
            onResult(Result.success(cached))
            return task
        }
        executor.execute {
            if (task.cancelled) return@execute
            val result = runCatching { String(fetch(url), Charsets.UTF_8) }
            result.getOrNull()?.let { textCache.put(url, it) }
            deliver(task, result, onResult)
        }
        return task
    }

    private fun <T> deliver(task: LoadTask, result: Result<T>, onResult: (Result<T>) -> Unit) {
        mainHandler.post {
            if (!task.cancelled) {
                onResult(result)
            }
        }
    }

    private fun fetch(url: String): ByteArray {
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = TIMEOUT_MS
            connection.readTimeout = TIMEOUT_MS
            val code = connection.responseCode
            if (code != HttpURLConnection.HTTP_OK) {
                throw IOException("HTTP $code")
            }
            return connection.inputStream.use { it.readBytes() }
        } finally {
            connection.disconnect()
        }
    }

    private fun decode(bytes: ByteArray, maxSize: Int): Bitmap {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
        var sample = 1
        while (bounds.outWidth / (sample * 2) >= maxSize && bounds.outHeight / (sample * 2) >= maxSize) {
            sample *= 2
        }
        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
            ?: throw IOException("decode failed")
    }
}
