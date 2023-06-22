package com.example.distanceapi.controller

import com.example.distanceapi.exception.DSCodeRegexException
import com.example.distanceapi.model.data.TrainStationDistanceDTO
import com.example.distanceapi.model.types.DistanceUnit
import com.example.distanceapi.model.types.TrainStationType
import com.example.distanceapi.service.DistanceCalculationService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/distance")
class DistanceController(val distanceCalculationService: DistanceCalculationService) {

    private val dsCodeRegex: Regex = """[A-Z ]{2,8}""".toRegex()

    @GetMapping("/{startStationID}/{endStationID}")
    @ResponseBody
    fun calculateDistance(
        @PathVariable startStationID: String,
        @PathVariable endStationID: String
    ): TrainStationDistanceDTO? {
        if (!dsCodeRegex.matches(startStationID)) throw DSCodeRegexException(startStationID)
        if (!dsCodeRegex.matches(endStationID)) throw DSCodeRegexException(endStationID)

        return distanceCalculationService.calculateDistanceTrainStation(
            startStationID,
            endStationID,
            TrainStationType.FV,
            DistanceUnit.KILOMETER
        )

    }

}