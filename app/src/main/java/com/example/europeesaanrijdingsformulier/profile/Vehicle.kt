package com.example.europeesaanrijdingsformulier.profile

import java.io.Serializable

class Vehicle(val id: Int,
              val country: String,
              val licensePlate: String,
              val brand: String,
              val model: String,
              val type: String): Serializable {
}