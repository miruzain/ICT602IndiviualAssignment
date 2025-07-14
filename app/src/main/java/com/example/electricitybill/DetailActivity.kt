package com.example.electricitybill

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var textMonth: TextView
    private lateinit var textUnits: TextView
    private lateinit var textTotalCharges: TextView
    private lateinit var textRebate: TextView
    private lateinit var textFinalCost: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        textMonth = findViewById(R.id.textMonth)
        textUnits = findViewById(R.id.textUnits)
        textTotalCharges = findViewById(R.id.textTotalCharges)
        textRebate = findViewById(R.id.textRebate)
        textFinalCost = findViewById(R.id.textFinalCost)

        val billId = intent.getIntExtra("billId", -1)
        if (billId != -1) {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val bill = db.electricityBillDao().getBillById(billId)
                bill?.let {
                    runOnUiThread {
                        textMonth.text = it.month
                        textUnits.text = it.units.toString()
                        textTotalCharges.text = "RM %.2f".format(it.totalCharges)
                        textRebate.text = "%.2f%%".format(it.rebate)
                        textFinalCost.text = "RM %.2f".format(it.finalCost)
                    }
                }
            }
        }
    }
}
