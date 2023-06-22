package com.example.distanceapi.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "csv-data-source")
class CSVDataSourceConfig(val fileName: String)