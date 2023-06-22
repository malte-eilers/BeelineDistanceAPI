package com.example.distanceapi.model

import com.example.distanceapi.config.CSVDataSourceConfig
import com.example.distanceapi.model.types.TrainStationType
import com.example.distanceapi.exception.TrainStationNotFoundException
import com.example.distanceapi.exception.TrainStationTypeException
import com.example.distanceapi.model.data.TrainStation
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.io.FileNotFoundException
import java.io.InputStream

@Repository
@Qualifier("CSVDatasource")
class TrainStationCSVRepository(val csvDataSourceConfig: CSVDataSourceConfig): TrainStationRepository {

    init {
        loadInputStream()
    }
    private fun loadInputStream(): InputStream = this::class.java.getResourceAsStream(csvDataSourceConfig.fileName) ?: throw FileNotFoundException("Datasource '${csvDataSourceConfig.fileName}' is missing!")

    @Override
    override fun findTrainStationByDSCode(id: String, stationType: TrainStationType): TrainStation {
        val trainStation = readCsvFile(id)
        trainStation.takeIf { it.stationType == stationType } ?: throw TrainStationTypeException(stationType, trainStation.stationType)
        return trainStation
    }

    private fun readCsvFile(requestedDSCode: String): TrainStation {
        val reader = loadInputStream().bufferedReader()
        reader.readLine()

        val trainStation = reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val line = it.split(';', ignoreCase = false, limit = 0)
                val dsCode = line[1]

                val dsCodeList = if (dsCode.contains(',')) {
                    dsCode.split(',')
                } else {
                    listOf<String>(dsCode)
                }

                TrainStation(dsCodeList, line[3], TrainStationType.createFromValue(line[4]), convertToFloat(line[5]), convertToFloat(line[6]))
            }.find { it.id.contains(requestedDSCode)} ?: throw TrainStationNotFoundException(requestedDSCode)
        reader.close()
        return trainStation
    }

    private fun convertToFloat(inputString: String): Double = inputString.replace(',', '.').toDouble()

}
