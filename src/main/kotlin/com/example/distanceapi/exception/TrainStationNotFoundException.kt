package com.example.distanceapi.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class TrainStationNotFoundException(dsCode: String) : Exception("Requested train station not found: Couldn't find station with DSCode100 '$dsCode'")