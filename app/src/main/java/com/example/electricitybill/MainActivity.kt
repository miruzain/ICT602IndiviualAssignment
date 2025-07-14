package com.example.electricitybill

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val months = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerMonth = findViewById<Spinner>(R.id.spinnerMonth)
        val editUnits = findViewById<EditText>(R.id.editUnits)
        val editRebate = findViewById<EditText>(R.id.editRebate)
        val buttonCalculate = findViewById<Button>(R.id.buttonCalculate)
        val buttonViewList = findViewById<Button>(R.id.buttonViewList)
        val buttonAbout = findViewById<Button>(R.id.buttonAbout)

        // Setup month spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = adapter

        // Set input types
        editUnits.inputType = InputType.TYPE_CLASS_NUMBER
        editRebate.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        // Input validation
        editUnits.doAfterTextChanged {
            val text = it.toString()
            if (text.isNotEmpty()) {
                val units = text.toIntOrNull()
                if (units == null || units < 0) {
                    editUnits.error = "Please enter a valid positive number"
                } else {
                    editUnits.error = null
                }
            }
        }

        editRebate.doAfterTextChanged {
            val text = it.toString()
            if (text.isNotEmpty()) {
                val rebate = text.toFloatOrNull()
                if (rebate == null || rebate < 0 || rebate > 5) {
                    editRebate.error = "Rebate must be between 0% and 5%"
                } else {
                    editRebate.error = null
                }
            }
        }

        buttonCalculate.setOnClickListener {
            calculateAndSave(spinnerMonth, editUnits, editRebate)
        }

        buttonViewList.setOnClickListener {
            val intent = Intent(this, ResultListActivity::class.java)
            startActivity(intent)
        }

        buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun calculateAndSave(spinnerMonth: Spinner, editUnits: EditText, editRebate: EditText) {
        val month = spinnerMonth.selectedItem.toString()
        val unitsText = editUnits.text.toString()
        val rebateText = editRebate.text.toString()

        if (unitsText.isEmpty()) {
            editUnits.error = "Please enter electricity units used"
            return
        }
        if (rebateText.isEmpty()) {
            editRebate.error = "Please enter rebate percentage"
            return
        }

        val units = unitsText.toIntOrNull()
        val rebate = rebateText.toFloatOrNull()

        if (units == null || units < 0) {
            editUnits.error = "Please enter a valid positive number"
            return
        }
        if (rebate == null || rebate < 0 || rebate > 5) {
            editRebate.error = "Rebate must be between 0% and 5%"
            return
        }

        val totalCharges = calculateCharges(units)
        val finalCost = totalCharges - (totalCharges * rebate / 100)

        val bill = ElectricityBill(
            month = month,
            units = units,
            totalCharges = totalCharges,
            rebate = rebate,
            finalCost = finalCost
        )

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.electricityBillDao().insert(bill)
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Bill saved for $month", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateCharges(units: Int): Double {
        var remainingUnits = units
        var charges = 0.0

        val blocks = listOf(
            200 to 0.218,
            100 to 0.334,
            300 to 0.516,
            Int.MAX_VALUE to 0.546
        )

        for ((blockUnits, rate) in blocks) {
            val usedUnits = if (remainingUnits > blockUnits) blockUnits else remainingUnits
            charges += usedUnits * rate
            remainingUnits -= usedUnits
            if (remainingUnits <= 0) break
        }

        return charges
    }
}
