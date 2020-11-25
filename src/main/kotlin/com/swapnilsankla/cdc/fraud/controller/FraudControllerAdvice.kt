package com.swapnilsankla.cdc.fraud.controller

import com.swapnilsankla.cdc.fraud.model.CustomerNotFoundException
import com.swapnilsankla.cdc.fraud.model.FraudCheck
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class FraudControllerAdvice {
	@ExceptionHandler(CustomerNotFoundException::class)
	fun onCustomerNotFoundException(): ResponseEntity<FraudCheck> {
		return ResponseEntity.notFound().build()
	}
}