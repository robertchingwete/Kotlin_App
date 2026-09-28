package com.example.app
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class CalculatorFragment : Fragment() {

    private lateinit var tvDisplay: TextView
    private var currentInput: String = ""
    private var operator: String? = null
    private var firstOperand: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculator, container, false)

        tvDisplay = view.findViewById(R.id.tv_display)

        val buttonIds = listOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        )
        for (id in buttonIds) {
            view.findViewById<Button>(id).setOnClickListener { appendNumber((it as Button).text.toString()) }
        }

        view.findViewById<Button>(R.id.btn_add).setOnClickListener { setOperator("+") }
        view.findViewById<Button>(R.id.btn_subtract).setOnClickListener { setOperator("-") }
        view.findViewById<Button>(R.id.btn_multiply).setOnClickListener { setOperator("*") }
        view.findViewById<Button>(R.id.btn_divide).setOnClickListener { setOperator("/") }
        view.findViewById<Button>(R.id.btn_equals).setOnClickListener { calculateResult() }
        view.findViewById<Button>(R.id.btn_clear).setOnClickListener { clear() }

        return view
    }

    private fun appendNumber(number: String) {
        currentInput += number
        tvDisplay.text = currentInput
    }

    private fun setOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            firstOperand = currentInput.toDouble()
            operator = op
            currentInput = ""
        }
    }

    private fun calculateResult() {
        if (currentInput.isNotEmpty() && operator != null && firstOperand != null) {
            val secondOperand = currentInput.toDouble()
            val result = when (operator) {
                "+" -> firstOperand!! + secondOperand
                "-" -> firstOperand!! - secondOperand
                "*" -> firstOperand!! * secondOperand
                "/" -> firstOperand!! / secondOperand
                else -> 0.0
            }
            tvDisplay.text = result.toString()
            currentInput = ""
            firstOperand = null
            operator = null
        }
    }

    private fun clear() {
        currentInput = ""
        firstOperand = null
        operator = null
        tvDisplay.text = "0"
    }
}
