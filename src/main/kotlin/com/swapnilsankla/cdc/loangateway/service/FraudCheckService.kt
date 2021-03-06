package com.swapnilsankla.cdc.loangateway.service

import com.swapnilsankla.cdc.loangateway.event.LoanApplicationEvent
import com.swapnilsankla.cdc.loangateway.model.FraudCheck
import com.swapnilsankla.cdc.loangateway.model.LoanApplication
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class FraudCheckService(
    @Value("\${loan-gateway.fraud-service-url}")
    private val fraudProviderUrl: String,
    private val restTemplate: RestTemplate,
    private val eventPublisher: ApplicationEventPublisher
) {
    fun isFraudulent(customerId: String): FraudCheck {
        val fraudCheck = try {
            val responseBody = restTemplate.getForEntity(
                fraudProviderUrl.replace("{customerId}", customerId),
                FraudCheck::class.java
            ).body
            FraudCheck(responseBody?.status ?: true)
        } catch (e: HttpClientErrorException) {
            FraudCheck(true)
        }
        eventPublisher.publishEvent(LoanApplicationEvent(LoanApplication(customerId, fraudCheck)))
        return fraudCheck
    }
}

