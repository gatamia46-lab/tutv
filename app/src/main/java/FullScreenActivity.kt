package com.example.tutv

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.tutv.databinding.ActivityFullScreenBinding

class FullScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenBinding
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ocultar barras del sistema para modo TV
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val videoUrl = intent.getStringExtra("VIDEO_URL")
        if (videoUrl != null) {
            setupPlayer(videoUrl)
        }
    }

    private fun setupPlayer(url: String) {
        player = ExoPlayer.Builder(this).build()
        binding.playerViewFull.player = player // Usa el ID del XML

        val mediaItem = MediaItem.fromUri(url)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}