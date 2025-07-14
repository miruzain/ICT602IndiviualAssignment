package com.example.electricitybill

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ResultListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var bills: List<ElectricityBill> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_list)

        listView = findViewById(R.id.listViewBills)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            bills = db.electricityBillDao().getAllBills()
            val displayList = bills.map { "${it.month}: RM %.2f".format(it.finalCost) }
            runOnUiThread {
                val adapter = ArrayAdapter(this@ResultListActivity, android.R.layout.simple_list_item_1, displayList)
                listView.adapter = adapter
            }
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedBill = bills[position]
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("billId", selectedBill.id)
            startActivity(intent)
        }
    }
}
