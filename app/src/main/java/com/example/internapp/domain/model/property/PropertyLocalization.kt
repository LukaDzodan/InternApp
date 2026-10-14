package com.example.internapp.domain.model.property


data class PropertyLocalization(
    val attachments: List<PropertyAttachment>,
    val title: String,
    val urls: List<PropertyUrlType>?,
    val primary: String
)
