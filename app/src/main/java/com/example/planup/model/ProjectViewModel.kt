package com.example.planup.model

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.planup.R

class ProjectViewModel: ViewModel() {
    // Estado para armazenar cores randomizadas para cada elemento (identificador -> cor)
    private val _colorMap = mutableStateMapOf<String, Color>()
    //val colorMap: Map<String, Color> = _colorMap

    private val _imageMap = mutableStateMapOf<String, Int>()
    //val imageMap: Map<String, Int> = _imageMap

    // Lista de cores disponíveis para randomizar
    private val colorList = listOf(
        Color.Red,
        Color.Green,
        Color(0XFF246BFD),
        Color.Gray,
        Color.Magenta
    )

    private val imageList = listOf(
        R.drawable.project_image_red,
        R.drawable.project_image02_red,
        R.drawable.project_image03_red,
        R.drawable.project_image04_red
    )

    // Função para obter ou gerar uma cor randomizada para cada elemento
    fun getColorForElement(element: String): Color {
        return _colorMap[element] ?: colorList.random().also { color ->
            _colorMap[element] = color // Armazena a cor randomizada
        }
    }

    fun getImageForElement(element: String): Int {
        return _imageMap[element] ?: imageList.random().also { image ->
            _imageMap[element] = image // Armazena a imagem randomizada
        }
    }
}