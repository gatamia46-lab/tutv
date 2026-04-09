package com.example.tutv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@OptIn(UnstableApi::class)
class MainActivity : AppCompatActivity() {
    private lateinit var adapter: VideoAdapter
    private lateinit var rv: RecyclerView
    private val listaVideos = mutableListOf<VideoModel>()
    private var exoPlayer: ExoPlayer? = null
    private var currentUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exoPlayer = ExoPlayer.Builder(this)
            .setLoadControl(DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    15_000, // Min buffer
                    50_000, // Max buffer
                    2_500,  // Buffer to start
                    5_000   // Buffer to resume
                )
                .build())
            .build()
        findViewById<PlayerView>(R.id.player_view).player = exoPlayer

        rv = findViewById(R.id.recyclerResultadosYoutube)
        rv.layoutManager = LinearLayoutManager(this)
        rv.descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS

        adapter = VideoAdapter(
            listaVideos,
            onItemClick = { v -> if (currentUrl == v.url) irAPantallaCompleta() else reproducir(v.url) },
            onItemFocus = { v -> if (currentUrl != v.url) reproducir(v.url) }
        )
        rv.adapter = adapter
        rv.setHasFixedSize(true) // Mejora la estabilidad del scroll y el foco

        findViewById<android.widget.Button>(R.id.btn_exit).setOnClickListener { finish() }

        findViewById<android.widget.Button>(R.id.btn_search_trigger).setOnClickListener {
            // Ahora abrimos la app de YouTube directamente ya que no hay cuadro de texto
            buscarEnYoutube("Sugeridos") 
        }

        cargarM3U()

        val splash = findViewById<View>(R.id.layout_splash)
        splash.postDelayed({
            splash.visibility = View.GONE
            rv.requestFocus()
            rv.postDelayed({
                val first = rv.findViewHolderForAdapterPosition(0)
                first?.itemView?.requestFocus()
                if (listaVideos.isNotEmpty() && currentUrl.isEmpty()) {
                    reproducir(listaVideos[0].url)
                }
            }, 400)
        }, 2500)
    }

    private fun buscarEnYoutube(query: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.youtube.com/results?search_query=${query.replace(" ", "+")}"))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al abrir YouTube", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reproducir(url: String) {
        currentUrl = url
        runOnUiThread {
            exoPlayer?.stop()
            
            // Dejamos que ExoPlayer detecte automáticamente el formato del stream
            // eliminando la restricción manual de MimeType para soportar más formatos
            val m = MediaItem.Builder()
                .setUri(url)
                .build()
            exoPlayer?.setMediaItem(m)
            exoPlayer?.prepare()
            exoPlayer?.play()
        }
    }

    private fun irAPantallaCompleta() {
        if (currentUrl.isNotEmpty()) {
            exoPlayer?.pause()
            val i = Intent(this, FullScreenActivity::class.java).apply {
                putExtra("VIDEO_URL", currentUrl)
            }
            startActivity(i)
        }
    }

    private fun cargarM3U() {
        try {
            assets.open("canales.txt").bufferedReader().useLines { lines ->
                var n = ""
                lines.forEach { l ->
                    if (l.startsWith("#EXTINF")) {
                        n = l.substringAfterLast(",")
                    } else if (l.startsWith("http")) {
                        listaVideos.add(VideoModel(n.trim(), l.trim()))
                        n = ""
                    }
                }
            }
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            android.util.Log.e("TuTV", "Error loading M3U: ${e.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentUrl.isNotEmpty()) exoPlayer?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }
}