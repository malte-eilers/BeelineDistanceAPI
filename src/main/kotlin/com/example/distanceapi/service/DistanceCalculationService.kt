package com.example.distanceapi.service

import com.example.distanceapi.model.data.TrainStationDistanceDTO
import com.example.distanceapi.model.types.DistanceUnit
import com.example.distanceapi.model.types.TrainStationType

interface DistanceCalculationService {
    fun calculateDistanceTrainStation(startStationID: String, endStationID: String, stationType: TrainStationType, measuringUnit: DistanceUnit): TrainStationDistanceDTO
}