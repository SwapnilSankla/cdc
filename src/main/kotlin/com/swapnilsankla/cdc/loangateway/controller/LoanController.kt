package com.swapnilsankla.cdc.loangateway.controller

import com.swapnilsankla.cdc.loangateway.service.FraudCheckService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loans")
class LoanController(val fraudCheckService: FraudCheckService) {
	@GetMapping("/apply/{customerId}")
	fun apply(@PathVariable customerId: String): ResponseEntity<String> {
		val fraudStatus = fraudCheckService.isFraudulent(customerId).status
		val response = if (fraudStatus) "We are sorry!" else "You will get it"
		return ResponseEntity.ok().body(response)
	}
}

