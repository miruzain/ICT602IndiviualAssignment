package com.example.electricitybill

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ElectricityBillDao {
    @Insert
    suspend fun insert(bill: ElectricityBill)

    @Query("SELECT * FROM electricity_bills ORDER BY id DESC")
    suspend fun getAllBills(): List<ElectricityBill>

    @Query("SELECT * FROM electricity_bills WHERE id = :id")
    suspend fun getBillById(id: Int): ElectricityBill?
}
