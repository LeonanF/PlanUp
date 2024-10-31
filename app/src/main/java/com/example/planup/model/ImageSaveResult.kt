package com.example.planup.model

import android.net.Uri

data class ImageSaveResult(
    val uri: Uri? = null,
    val success: Boolean,
    val errorMessage: String? = null
)
