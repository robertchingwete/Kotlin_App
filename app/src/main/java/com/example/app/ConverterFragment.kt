package com.example.app
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class ConverterFragment : Fragment() {

    private lateinit var spinnerFromUnit: Spinner
    private lateinit var spinnerToUnit: Spinner
    private lateinit var inputValue: EditText
    private lateinit var tvConvertedResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_converter, container, false)

        // Initialize views
        spinnerFromUnit = view.findViewById(R.id.spinner_from_unit)
        spinnerToUnit = view.findViewById(R.id.spinner_to_unit)
        inputValue = view.findViewById(R.id.input_value)
        tvConvertedResult = view.findViewById(R.id.tv_converted_result)

        // Set listeners for real-time conversion
        setupListeners()

        return view
    }

    private fun setupListeners() {
        inputValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                convertValue()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        spinnerFromUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertValue()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerToUnit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertValue()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun convertValue() {
        val input = inputValue.text.toString()
        if (input.isEmpty()) {
            tvConvertedResult.text = getString(R.string.converted_result_placeholder)
            return
        }

        val fromUnit = spinnerFromUnit.selectedItem.toString()
        val toUnit = spinnerToUnit.selectedItem.toString()
        val value = input.toDouble()

        val result = when (fromUnit to toUnit) {
            "Meter" to "Kilometer" -> value / 1000
            "Kilometer" to "Meter" -> value * 1000
            "Gram" to "Kilogram" -> value / 1000
            "Kilogram" to "Gram" -> value * 1000
            "Celsius" to "Fahrenheit" -> (value * 9 / 5) + 32
            "Fahrenheit" to "Celsius" -> (value - 32) * 5 / 9
            else -> 0.0 // Unsupported conversion
        }

        tvConvertedResult.text = result.toString()
    }
}
