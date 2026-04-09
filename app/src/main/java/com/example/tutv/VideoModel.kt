package com.example.tutv

data class VideoModel(
    val nombre: String,
    val url: String,
    val thumbnail: String = ""
)