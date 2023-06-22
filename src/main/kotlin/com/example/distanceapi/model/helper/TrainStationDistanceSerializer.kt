package com.example.distanceapi.model.helper

import com.example.distanceapi.model.data.TrainStationDistanceDTO
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import java.io.IOException

@JsonComponent
class TrainStationDistanceSerializer: JsonSerializer<TrainStationDistanceDTO>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(trainStationDistanceDTO: TrainStationDistanceDTO?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.let { jGen ->
            trainStationDistanceDTO?.let { value ->
                jGen.writeStartObject();
                jGen.writeStringField("from", value.startStation.name)
                jGen.writeStringField("to",  value.endStation.name)
                jGen.writeNumberField("distance", value.distance)
                jGen.writeStringField("unit",  value.unit.measuringUnit)
                jGen.writeEndObject();
            } ?: jGen.writeNull()
        }
    }
}