package com.example.distanceapi.controller

import com.example.distanceapi.exception.TrainStationNotFoundException
import com.example.distanceapi.exception.TrainStationTypeException
import com.example.distanceapi.model.TrainStationRepository
import com.example.distanceapi.model.data.TrainStation
import com.example.distanceapi.model.data.TrainStationDistanceDTO
import com.example.distanceapi.model.types.DistanceUnit
import com.example.distanceapi.model.types.TrainStationType
import com.example.distanceapi.service.DistanceCalculationService
import com.example.distanceapi.service.HaversineDistanceCalculationService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
internal class DistanceControllerTest() {

    @Autowired lateinit var mvc: MockMvc

    private val dataRepository: TrainStationRepository = mockk<TrainStationRepository>()
    private val calculationService: HaversineDistanceCalculationService = HaversineDistanceCalculationService(dataRepository)

    private val calculationServiceMockk: DistanceCalculationService = HaversineDistanceCalculationService(dataRepository)

    private val trainStationTestOne: TrainStation = TrainStation(listOf("FF"), "Frankfurt(Main)Hbf", TrainStationType.FV, 8.663789, 50.107145)
    private val trainStationTestTwo: TrainStation = TrainStation(listOf("BLS"), "Berlin", TrainStationType.FV, 13.369545, 52.525592)
    private val trainStationTestOnlyRV: TrainStation = TrainStation(listOf("BJUF"), "Berlin Jungfernheide", TrainStationType.RV, 13.299437, 52.530276)

    @Test
    fun `Unit Test - Distance Controller`() {
        val distanceController: DistanceController = DistanceController(calculationServiceMockk)

        every { calculationServiceMockk.calculateDistanceTrainStation("FF", "BLS", TrainStationType.FV, DistanceUnit.KILOMETER) } returns TrainStationDistanceDTO(trainStationTestOne, trainStationTestTwo, 423, DistanceUnit.KILOMETER)

        mvc.get("/api/v1/distance/FF/BLS").andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("{\"from\":\"Frankfurt(Main)Hbf\",\"to\":\"Berlin Hbf\",\"distance\":423,\"unit\":\"km\"}") }
        }
    }

    @Test
    fun `Integration Test - Distance Controller`() {
        val distanceController: DistanceController = DistanceController(calculationService)

        every { dataRepository.findTrainStationByDSCode("FF", TrainStationType.FV) } returns trainStationTestOne
        every { dataRepository.findTrainStationByDSCode("BLS", TrainStationType.FV) } returns trainStationTestTwo

        mvc.get("/api/v1/distance/FF/BLS")
            .andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("{\"from\":\"Frankfurt(Main)Hbf\",\"to\":\"Berlin Hbf\",\"distance\":423,\"unit\":\"km\"}") }
        }
    }

    @Test
    fun `Unit Test - DS100 Code can't be found`() {
        val distanceController: DistanceController = DistanceController(calculationServiceMockk)

        every { calculationServiceMockk.calculateDistanceTrainStation("FF", "BTD", TrainStationType.FV, DistanceUnit.KILOMETER) } throws TrainStationNotFoundException("BTD")

        val responseOne = mvc.get("/api/v1/distance/FF/BTD").andExpect {
            status { isNotFound() }
        }

        every { calculationServiceMockk.calculateDistanceTrainStation("BTD", "FF", TrainStationType.FV, DistanceUnit.KILOMETER) } throws TrainStationNotFoundException("BTD")

        val responseTwo = mvc.get("/api/v1/distance/BTD/FF").andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `Integration Test - DS100 Code can't be found`() {
        val distanceController: DistanceController = DistanceController(calculationService)

        every { dataRepository.findTrainStationByDSCode("FF", TrainStationType.FV) } returns trainStationTestOne
        every { dataRepository.findTrainStationByDSCode("BTD", TrainStationType.FV) } throws TrainStationNotFoundException("BTD")

        val responseOne = mvc.get("/api/v1/distance/FF/BTD").andExpect {
            status { isNotFound() }
        }

        every { dataRepository.findTrainStationByDSCode("BTD", TrainStationType.FV) } throws TrainStationNotFoundException("BTD")
        every { dataRepository.findTrainStationByDSCode("FF", TrainStationType.FV) } returns trainStationTestOne

        val responseTwo = mvc.get("/api/v1/distance/BTD/FF").andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `Unit Test - Wrong train station type`() {
        val distanceController: DistanceController = DistanceController(calculationServiceMockk)

        every { calculationServiceMockk.calculateDistanceTrainStation("FF", "BJUF", TrainStationType.FV, DistanceUnit.KILOMETER) } throws TrainStationTypeException(TrainStationType.FV, TrainStationType.RV)

        val responseOne = mvc.get("/api/v1/distance/FF/BJUF").andExpect {
            status { isNotFound() }
        }

        every { calculationServiceMockk.calculateDistanceTrainStation("BJUF", "FF", TrainStationType.FV, DistanceUnit.KILOMETER) } throws TrainStationTypeException(TrainStationType.FV, TrainStationType.RV)

        val responseTwo = mvc.get("/api/v1/distance/BJUF/FF").andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `Integration Test - Wrong train station type`() {
        val distanceController: DistanceController = DistanceController(calculationService)

        every { dataRepository.findTrainStationByDSCode("FF", TrainStationType.FV) } returns trainStationTestOne
        every { dataRepository.findTrainStationByDSCode("BJUF", TrainStationType.RV) } throws TrainStationTypeException(TrainStationType.FV, TrainStationType.RV)

        val responseOne = mvc.get("/api/v1/distance/FF/BJUF").andExpect {
            status { isNotFound() }
        }

        every { dataRepository.findTrainStationByDSCode("BJUF", TrainStationType.RV) } throws TrainStationTypeException(TrainStationType.FV, TrainStationType.RV)
        every { dataRepository.findTrainStationByDSCode("FF", TrainStationType.FV) } returns trainStationTestOne

        val responseTwo = mvc.get("/api/v1/distance/BJUF/FF").andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `Unit Test - Wrong DS100 Regex`() {
        val responseOne = mvc.get("/api/v1/distance/A/FF").andExpect {
            status { isBadRequest() }
        }

        val responseTwo = mvc.get("/api/v1/distance/FF/A").andExpect {
            status { isBadRequest() }
        }
    }
}