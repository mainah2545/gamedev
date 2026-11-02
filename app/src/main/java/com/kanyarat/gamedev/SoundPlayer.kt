package com.kanyarat.gamedev

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundPlayer(context: Context) {

    private val pool: SoundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    @Volatile
    private var loaded = false
    private val soundId: Int

    init {
        pool.setOnLoadCompleteListener { _, _, status -> loaded = status == 0 }
        soundId = pool.load(context.applicationContext, R.raw.slide, 1)
    }

    fun play() {
        if (loaded) {
            pool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        pool.release()
    }
}
