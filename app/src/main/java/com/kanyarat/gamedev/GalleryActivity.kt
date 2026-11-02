package com.kanyarat.gamedev

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import kotlin.random.Random

class GalleryActivity : BaseActivity() {

    private lateinit var pager: ViewPager2
    private lateinit var counter: TextView
    private lateinit var soundButton: Button
    private lateinit var sound: SoundPlayer

    private val handler = Handler(Looper.getMainLooper())
    private val total = RemoteUrls.GALLERY_COUNT
    private var soundOn = true
    private var autoStopped = false
    private var lastPosition = -1

    private val autoSlide = object : Runnable {
        override fun run() {
            pager.setCurrentItem(pager.currentItem + 1, true)
            handler.postDelayed(this, AUTO_DELAY_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        pager = findViewById(R.id.galleryPager)
        counter = findViewById(R.id.galleryCounter)
        soundButton = findViewById(R.id.soundButton)
        sound = SoundPlayer(this)

        if (total <= 0) {
            findViewById<View>(R.id.galleryEmpty).visibility = View.VISIBLE
            findViewById<View>(R.id.galleryControls).visibility = View.GONE
            pager.visibility = View.GONE
            counter.visibility = View.GONE
            return
        }

        soundOn = savedInstanceState?.getBoolean(KEY_SOUND, true) ?: true
        autoStopped = savedInstanceState?.getBoolean(KEY_AUTO_STOPPED, false) ?: false
        val savedIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        val index = if (savedIndex in 0 until total) savedIndex else 0

        pager.adapter = GalleryAdapter()
        pager.setPageTransformer(GalleryTransformer())
        val start = START_POSITION - START_POSITION % total + index
        pager.setCurrentItem(start, false)
        lastPosition = start
        updateCounter(start)
        updateSoundButton()

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    autoStopped = true
                    handler.removeCallbacks(autoSlide)
                }
            }

            override fun onPageSelected(position: Int) {
                if (position == lastPosition) return
                lastPosition = position
                updateCounter(position)
                playSound()
                if (!autoStopped) scheduleAutoSlide()
            }
        })

        findViewById<Button>(R.id.prevButton).setOnClickListener {
            pager.setCurrentItem(pager.currentItem - 1, true)
        }
        findViewById<Button>(R.id.nextButton).setOnClickListener {
            pager.setCurrentItem(pager.currentItem + 1, true)
        }
        findViewById<Button>(R.id.randomButton).setOnClickListener { goRandom() }
        soundButton.setOnClickListener {
            soundOn = !soundOn
            updateSoundButton()
        }
    }

    private fun goRandom() {
        val current = pager.currentItem
        val target = current - current % total + Random.nextInt(total)
        if (target == current) {
            playSound()
        } else {
            pager.setCurrentItem(target, true)
        }
    }

    private fun playSound() {
        if (soundOn) sound.play()
    }

    private fun updateCounter(position: Int) {
        counter.text = getString(R.string.gallery_counter, position % total + 1, total)
    }

    private fun updateSoundButton() {
        soundButton.setText(if (soundOn) R.string.sound_on else R.string.sound_off)
    }

    private fun scheduleAutoSlide() {
        handler.removeCallbacks(autoSlide)
        handler.postDelayed(autoSlide, AUTO_DELAY_MS)
    }

    override fun onResume() {
        super.onResume()
        if (total > 0 && !autoStopped) scheduleAutoSlide()
    }

    override fun onPause() {
        handler.removeCallbacks(autoSlide)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (total > 0) {
            outState.putInt(KEY_INDEX, pager.currentItem % total)
        }
        outState.putBoolean(KEY_SOUND, soundOn)
        outState.putBoolean(KEY_AUTO_STOPPED, autoStopped)
    }

    override fun onDestroy() {
        handler.removeCallbacks(autoSlide)
        sound.release()
        super.onDestroy()
    }

    private companion object {
        const val AUTO_DELAY_MS = 3000L
        const val START_POSITION = Int.MAX_VALUE / 2
        const val KEY_INDEX = "gallery_index"
        const val KEY_SOUND = "gallery_sound"
        const val KEY_AUTO_STOPPED = "gallery_auto_stopped"
    }
}
