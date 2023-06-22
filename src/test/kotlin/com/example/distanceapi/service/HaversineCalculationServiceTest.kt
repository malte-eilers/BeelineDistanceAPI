package com.example.distanceapi.service

import com.example.distanceapi.model.TrainStationRepository
import com.example.distanceapi.model.data.TrainStation
import com.example.distanceapi.model.data.TrainStationDistanceDTO
import com.example.distanceapi.model.types.DistanceUnit
import com.example.distanceapi.model.types.TrainStationType
import io.mockk.MockK
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class HaversineCalculationServiceTest {

    private val dataRepository: TrainStationRepository = mockk<TrainStationRepository>()
    private val trainStationTestOne: TrainStation = TrainStation(listOf("FF"), "Frankfurt", TrainStationType.FV, 8.663789, 50.107145)
    private val trainStationTestTwo: TrainStation = TrainStation(listOf("BLS"), "Berlin", TrainStationType.FV, 13.369545, 52.525592)

    @Test
    fun `Unit Test - Successful calculation of the haversine distance`() {
        val calculationService: HaversineDistanceCalculationService = HaversineDistanceCalculationService(dataRepository)

        every { dataRepository.findTrainStationByDSCode("FF", TrainStationType.FV) } returns trainStationTestOne
        every { dataRepository.findTrainStationByDSCode("BLS", TrainStationType.FV) } returns trainStationTestTwo

        assertEquals(calculationService.calculateDistanceTrainStation("FF", "BLS", TrainStationType.FV, DistanceUnit.KILOMETER), TrainStationDistanceDTO(trainStationTestOne, trainStationTestTwo, 423, DistanceUnit.KILOMETER))
    }

    @Test
    fun `Unit Test - Rejected Calculation due to wrong StationType`() {
        val calculationService: HaversineDistanceCalculationService = HaversineDistanceCalculationService(dataRepository)

        every { dataRepository.findTrainStationByDSCode("FF", TrainStationType.FV) } returns trainStationTestOne
        every { dataRepository.findTrainStationByDSCode("BLS", TrainStationType.FV) } returns trainStationTestTwo

        assertEquals(calculationService.calculateDistanceTrainStation("FF", "BLS", TrainStationType.FV, DistanceUnit.KILOMETER), TrainStationDistanceDTO(trainStationTestOne, trainStationTestTwo, 423, DistanceUnit.KILOMETER))
    }
}