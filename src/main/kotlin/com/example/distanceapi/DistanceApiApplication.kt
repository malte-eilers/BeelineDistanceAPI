package com.example.distanceapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@SpringBootApplication
@ConfigurationPropertiesScan
class DistanceApiApplication

fun main(args: Array<String>) {
    runApplication<DistanceApiApplication>(*args)
}