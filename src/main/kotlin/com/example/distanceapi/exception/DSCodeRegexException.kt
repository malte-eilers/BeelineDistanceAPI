package com.example.distanceapi.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class DSCodeRegexException(dsCode: String) : Exception("The requested DS100 Code '$dsCode' does not comply with the Requirement (Only uppercase letters and blank spaces between two and six characters allowed)")
