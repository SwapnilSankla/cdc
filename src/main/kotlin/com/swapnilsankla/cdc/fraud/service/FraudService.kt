package com.swapnilsankla.cdc.fraud.service

import com.swapnilsankla.cdc.fraud.model.FraudCheck
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
class FraudService {
	fun fraudStatus(customerId: String): FraudCheck {
		TODO("Not yet implemented")
	}
}