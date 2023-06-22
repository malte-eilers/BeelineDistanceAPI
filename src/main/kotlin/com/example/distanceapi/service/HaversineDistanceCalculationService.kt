package com.example.distanceapi.service

import com.example.distanceapi.model.types.DistanceUnit
import com.example.distanceapi.model.data.TrainStation
import com.example.distanceapi.model.data.TrainStationDistanceDTO
import com.example.distanceapi.model.types.TrainStationType
import com.example.distanceapi.model.TrainStationRepository
import org.springframework.stereotype.Service
import kotlin.math.pow

@Service
class HaversineDistanceCalculationService(private val dataSource: TrainStationRepository) : DistanceCalculationService {

    private val earthSphericalRadius: Float = 6371.0f

    override fun calculateDistanceTrainStation(startStationID: String, endStationID: String, stationType: TrainStationType, measuringUnit: DistanceUnit): TrainStationDistanceDTO {
        val startTrainStation = dataSource.findTrainStationByDSCode(startStationID, stationType)
        val endTrainStation = dataSource.findTrainStationByDSCode(endStationID, stationType)

        val distance = calculateHaversineDistance(startTrainStation, endTrainStation, measuringUnit)
        return TrainStationDistanceDTO(startTrainStation, endTrainStation, distance, measuringUnit)
    }

    private fun calculateHaversineDistance(startStation: TrainStation, endStation: TrainStation, measuringUnit: DistanceUnit): Int {
        val startStationLatitudeCosine: Double = kotlin.math.cos(Math.toRadians(startStation.latitude))
        val endStationLatitudeCosine: Double = kotlin.math.cos(Math.toRadians(endStation.latitude))

        val resultMultiplicationTerm: Double = startStationLatitudeCosine * endStationLatitudeCosine * calculateHaversineFunction(startStation.longitude, endStation.longitude)
        val inverseHaversine = kotlin.math.sqrt(calculateHaversineFunction(startStation.latitude, endStation.latitude) + resultMultiplicationTerm)

        val distance = 2 * earthSphericalRadius * kotlin.math.asin(inverseHaversine)
        return convertToMeasuringUnit(measuringUnit, distance)
    }

    private fun calculateHaversineFunction(startCoordinate: Double, endCoordinate: Double): Double {
        val sineResult = kotlin.math.sin(Math.toRadians((endCoordinate - startCoordinate) / 2.0))
        return sineResult.pow(2)
    }

    private fun convertToMeasuringUnit(measuringUnit: DistanceUnit, value: Double): Int {
        return when (measuringUnit) {
            DistanceUnit.MILES -> kotlin.math.round(value * 1.6).toInt()
            else -> kotlin.math.round(value).toInt()
        }
    }

}