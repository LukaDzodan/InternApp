package com.example.internapp.domain.model.property

data class PropertyAttachment(
    val `file`: String,
    val type: AttachmentType,
    val url: String
)
