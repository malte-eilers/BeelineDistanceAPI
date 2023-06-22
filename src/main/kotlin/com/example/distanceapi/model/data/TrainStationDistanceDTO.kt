package com.example.distanceapi.model.data

import com.example.distanceapi.model.data.TrainStation
import com.example.distanceapi.model.helper.TrainStationDistanceSerializer
import com.example.distanceapi.model.types.DistanceUnit
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(keyUsing = TrainStationDistanceSerializer::class)
data class TrainStationDistanceDTO(val startStation: TrainStation, val endStation: TrainStation, val distance: Int, val unit: DistanceUnit)