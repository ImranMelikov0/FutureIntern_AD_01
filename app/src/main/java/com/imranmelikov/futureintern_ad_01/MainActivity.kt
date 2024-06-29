package com.imranmelikov.futureintern_ad_01

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.imranmelikov.futureintern_ad_01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var currencyFrom="m"
    private var currencyTo="cm"
    private val units = arrayOf("m", "kg", "cm", "mm", "g")
    private lateinit var fromSpinner: Spinner
    private lateinit var toSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editText=binding.edittext.text
        fromSpinner=binding.spinnerFrom
        toSpinner=binding.spinnerTo
        spinner()
        clickResetBtn(editText)
        clickConvertBtn(editText)


    }

    private fun clickConvertBtn(value: Editable){
        binding.convertBtn.setOnClickListener {
            if (value.trim().isEmpty()){
                Toast.makeText(this, "Please enter a valid value", Toast.LENGTH_SHORT).show()
            }else{
                performConversion(value)
            }
        }
    }
    private fun performConversion(editText: Editable) {
        val value = editText.toString().toDoubleOrNull()
        val fromUnit = fromSpinner.selectedItem.toString()
        val toUnit = toSpinner.selectedItem.toString()

        if (value == null) {
            Toast.makeText(this, "Please enter a valid value", Toast.LENGTH_SHORT).show()
            return
        }

        if (isInvalidConversion(fromUnit, toUnit)) {
            Toast.makeText(this, "Invalid conversion from $fromUnit to $toUnit", Toast.LENGTH_SHORT).show()
            return
        }

        val result = convert(value, fromUnit, toUnit)
        binding.resultText.text = result.toString()
    }

    private fun clickResetBtn(value: Editable){
        binding.resetBtn.setOnClickListener {
            value.clear()
            binding.resultText.text="0"
        }
    }

    private fun spinner(){
        val adapter2= ArrayAdapter(this,R.layout.spinner_item,units)
        adapter2.setDropDownViewResource(R.layout.spinner_item)
        toSpinner.adapter=adapter2
        toSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                currencyTo=parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        val adapter= ArrayAdapter(this,R.layout.spinner_item,units)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        fromSpinner.adapter=adapter
        fromSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                currencyFrom=parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.convertIcon.setOnClickListener {
            fromSpinner.setSelection(adapter.getPosition(currencyTo))
            toSpinner.setSelection(adapter2.getPosition(currencyFrom))
        }

    }

    private fun isInvalidConversion(from: String, to: String): Boolean {
        val validConversions = mapOf(
            "m" to listOf("cm", "mm","m"),
            "cm" to listOf("m", "mm","cm"),
            "mm" to listOf("m", "cm","mm"),
            "kg" to listOf("g","kg"),
            "g" to listOf("kg","g")
        )

        return !(validConversions[from]?.contains(to) ?: false)
    }

    private fun convert(value: Double, from: String, to: String): Double {
        return when (from to to) {
            "m" to "m" -> value * 1
            "cm" to "cm" -> value * 1
            "mm" to "mm" -> value * 1
            "kg" to "kg" -> value * 1
            "g" to "g" -> value * 1
            "m" to "cm" -> value * 100
            "m" to "mm" -> value * 1000
            "cm" to "m" -> value / 100
            "cm" to "mm" -> value * 10
            "mm" to "m" -> value / 1000
            "mm" to "cm" -> value / 10
            "kg" to "g" -> value * 1000
            "g" to "kg" -> value / 1000
            else -> value
        }
    }

}