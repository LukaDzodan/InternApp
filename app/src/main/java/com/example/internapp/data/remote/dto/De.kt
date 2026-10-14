package com.example.internapp.data.remote.dto

data class De(
    val attachments: List<com.example.internapp.data.remote.dto.Attachment>,
    val text: com.example.internapp.data.remote.dto.Text,
    val urls: List<com.example.internapp.data.remote.dto.Url>?,
)
