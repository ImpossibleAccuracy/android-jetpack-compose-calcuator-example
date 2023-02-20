package com.example.myapplication.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.myapplication.model.ButtonData
import com.example.myapplication.model.ButtonType
import kotlin.math.ceil
import kotlin.math.min

fun getColorByButtonType(type: ButtonType): Color {
    return when (type) {
        ButtonType.NUMBER -> Color.Cyan
        ButtonType.DELIMITER -> Color.Cyan
        ButtonType.ACTION -> Color.Magenta
        ButtonType.OPERATION -> Color.Green
    }
}

@Composable
fun DataControls(
    cols: Int,
    screenWidth: Dp,
    spacing: Dp,
    data: ArrayList<ButtonData>,
    onBtnClick: (btn: ButtonData) -> Unit
) {
    val rows = ceil(data.size.toDouble() / cols).toInt()

    val width = screenWidth - spacing * cols * 2
    val size = width / cols

    Column {
        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                val rangeStart = row * cols
                val rangeEnd = min((row + 1) * cols, data.size)

                for (pos in rangeStart until rangeEnd) {
                    val item = data[pos]

                    val itemWidth = size * item.width + spacing * 2 * (item.width - 1)

                    SquareButton(
                        Modifier
                            .padding(spacing)
                            .width(itemWidth)
                            .height(size),
                        text = item.text,
                        color = getColorByButtonType(item.type),
                        onClick = {
                            onBtnClick(item)
                        }
                    )
                }
            }
        }
    }
}