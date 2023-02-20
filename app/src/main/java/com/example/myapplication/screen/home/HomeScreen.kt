package com.example.myapplication.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.ButtonData
import com.example.myapplication.model.ButtonType
import kotlin.math.roundToInt

class State {
    companion object {
        const val cols = 4
        const val spacing = 6

        val buttons = ArrayList<ButtonData>()

        init {
            buttons += ButtonData("AC", ButtonType.ACTION)
            buttons += ButtonData("+/-", ButtonType.ACTION)
            buttons += ButtonData("%", ButtonType.ACTION)
            buttons += ButtonData("/", ButtonType.OPERATION)

            buttons += ButtonData("7", ButtonType.NUMBER)
            buttons += ButtonData("8", ButtonType.NUMBER)
            buttons += ButtonData("9", ButtonType.NUMBER)
            buttons += ButtonData("*", ButtonType.OPERATION)

            buttons += ButtonData("4", ButtonType.NUMBER)
            buttons += ButtonData("5", ButtonType.NUMBER)
            buttons += ButtonData("6", ButtonType.NUMBER)
            buttons += ButtonData("-", ButtonType.OPERATION)

            buttons += ButtonData("1", ButtonType.NUMBER)
            buttons += ButtonData("2", ButtonType.NUMBER)
            buttons += ButtonData("3", ButtonType.NUMBER)
            buttons += ButtonData("+", ButtonType.OPERATION)

            buttons += ButtonData("0", ButtonType.NUMBER, 2)
            buttons += ButtonData(",", ButtonType.DELIMITER)
            buttons += ButtonData("=", ButtonType.OPERATION)
        }
    }

    var currentNumber by mutableStateOf("")
    var currentOperation by mutableStateOf("")

    private val stack = ArrayList<Float>()

    fun handleNumber(btn: ButtonData) {
        this.currentNumber += btn.text
    }

    fun handleDelimiter() {
        this.currentNumber = this.currentNumber.replace(".", "") + "."
    }

    fun handleOperation(btn: ButtonData) {
        if (this.currentNumber.isEmpty() ||
            (this.currentNumber.length == 1 && this.currentNumber[0] == '-')
        ) {
            if (btn.text == "+") {
                convertNumberToPositive()
            } else if (btn.text == "-") {
                this.convertNumberToOpposite()
            }
        } else {
            if (this.stack.size == 1 || btn.text != "=") {
                this.stack.add(0, this.currentNumber.toFloat())
            }

            if (btn.text == "=") {
                onEqualsOperator()
            } else {
                this.currentOperation = btn.text
                this.currentNumber = ""
            }
        }
    }

    fun handleAction(btn: ButtonData) {
        if (btn.text == "AC") {
            if (this.currentNumber.isEmpty()) {
                this.currentOperation = ""
                this.stack.clear()
            } else {
                this.currentNumber = ""
            }
        } else if (btn.text == "+/-") {
            this.convertNumberToOpposite()
        }
    }

    private fun updateResult(data: Float) {
        this.currentNumber =
            if (data.roundToInt().toFloat() == data) {
                data.toInt().toString()
            } else {
                data.toString()
            }
    }

    private fun onEqualsOperator() {
        if (this.stack.size > 1) {
            val num1 = this.stack[1]
            val num2 = this.stack[0]

            this.stack.removeAt(1)
            this.stack.removeAt(0)

            val result = when (this.currentOperation) {
                "+" -> 1f * num1 + num2
                "-" -> 1f * num1 - num2
                "*" -> 1f * num1 * num2
                "/" -> 1f * num1 / num2
                else -> Float.MIN_VALUE
            }

            updateResult(result)

            this.currentOperation = ""
        }
    }

    private fun convertNumberToPositive(): Boolean {
        if (this.isFirstSymbolMinus()) {
            this.currentNumber = currentNumber.slice(1 until currentNumber.length)
            return true
        }

        return false
    }

    private fun convertNumberToOpposite() {
        if (!convertNumberToPositive()) {
            this.currentNumber = "-$currentNumber"
        }
    }

    private fun isFirstSymbolMinus(): Boolean {
        return this.currentNumber.isNotEmpty() &&
                this.currentNumber[0] == '-'
    }
}

@Composable
fun HomeScreen() {
    val cols = State.cols
    val spacing = State.spacing.dp
    val buttons = State.buttons

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val state by remember { mutableStateOf(State()) }

    Column {
        DataResult(
            text = state.currentNumber,
            modifier = Modifier
                .weight(1f)
                .padding(spacing * 2, spacing)
        )

        DataControls(
            cols = cols,
            screenWidth = screenWidth,
            spacing = spacing,
            data = buttons,
            onBtnClick = {
                when (it.type) {
                    ButtonType.NUMBER -> {
                        state.handleNumber(it)
                    }
                    ButtonType.DELIMITER -> {
                        state.handleDelimiter()
                    }
                    ButtonType.OPERATION -> {
                        state.handleOperation(it)
                    }
                    ButtonType.ACTION -> {
                        state.handleAction(it)
                    }
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HomeScreen()
}