package com.example.myapplication.model

enum class ButtonType {
    NUMBER,
    ACTION,
    OPERATION,
    DELIMITER
}

data class ButtonData(
    val text: String,
    val type: ButtonType,
    val width: Int = 1
)
