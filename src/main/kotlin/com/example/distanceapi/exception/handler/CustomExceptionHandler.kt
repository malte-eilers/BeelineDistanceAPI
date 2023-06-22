package com.example.distanceapi.exception.handler

import com.example.distanceapi.exception.DSCodeRegexException
import com.example.distanceapi.exception.TrainStationNotFoundException
import com.example.distanceapi.exception.TrainStationTypeException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest


@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun createServerErrorGate(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        when (ex) {
            is TrainStationNotFoundException,
            is TrainStationTypeException,
            is DSCodeRegexException-> throw ex

            else -> return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There is an issue with the server. Please try again later")
        }
    }
}