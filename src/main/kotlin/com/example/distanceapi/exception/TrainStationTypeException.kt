package com.example.distanceapi.exception

import com.example.distanceapi.model.types.TrainStationType
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class TrainStationTypeException(allowedStationType: TrainStationType, requestedStationType: TrainStationType) :
    Exception("Requested train station has the wrong station type (Only ${allowedStationType.name} possible - station is ${requestedStationType.name})")