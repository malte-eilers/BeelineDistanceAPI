package com.example.distanceapi.model

import com.example.distanceapi.model.data.TrainStation
import com.example.distanceapi.model.types.TrainStationType

interface TrainStationRepository {
    fun findTrainStationByDSCode(id: String, stationType: TrainStationType): TrainStation
}