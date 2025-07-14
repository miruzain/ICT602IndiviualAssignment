package com.example.electricitybill

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "electricity_bills")
data class ElectricityBill(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val month: String,
    val units: Int,
    val totalCharges: Double,
    val rebate: Float,
    val finalCost: Double
)
