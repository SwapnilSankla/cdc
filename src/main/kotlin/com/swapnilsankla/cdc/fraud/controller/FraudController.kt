package com.swapnilsankla.cdc.fraud.controller

import com.swapnilsankla.cdc.fraud.model.FraudCheck
import com.swapnilsankla.cdc.fraud.service.FraudService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fraud")
class FraudController(@Autowired private val fraudService: FraudService) {
	@GetMapping("{customerId}")
	fun apply(@PathVariable customerId: String): ResponseEntity<FraudCheck> {
		val fraudStatus = fraudService.fraudStatus(customerId)
		return ResponseEntity.ok(fraudStatus)
	}
}