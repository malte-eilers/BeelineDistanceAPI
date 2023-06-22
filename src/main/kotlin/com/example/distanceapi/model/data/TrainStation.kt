package com.example.distanceapi.model.data

import com.example.distanceapi.model.types.TrainStationType

data class TrainStation(val id: List<String>, val name: String, val stationType: TrainStationType, val longitude: Double, val latitude: Double)