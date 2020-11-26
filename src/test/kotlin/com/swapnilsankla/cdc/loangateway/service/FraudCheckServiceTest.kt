package com.swapnilsankla.cdc.loangateway.service

import com.swapnilsankla.cdc.loangateway.model.FraudCheck
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

class FraudCheckServiceTest {
    private val eventPublisher = ApplicationEventPublisher {
        // do nothing
    }

    @Test
    fun `should receive fraud status from Fraud data provider`() {
        val restTemplate = mockk<RestTemplate> {
            every { getForEntity("http://localhost:8080/fraud/1", FraudCheck::class.java) } returns ResponseEntity.ok(
                FraudCheck(false)
            )
        }
        val fraudCheckService = FraudCheckService("http://localhost:8080/fraud/{customerId}", restTemplate, eventPublisher)

        fraudCheckService.isFraudulent("1").status shouldBe false
    }

    @Test
    fun `return fraud status as true if data is unavailable at fraud provider`() {
        val restTemplate = mockk<RestTemplate> {
            every {
                getForEntity(
                    "http://localhost:8080/fraud/1",
                    FraudCheck::class.java
                )
            } throws HttpClientErrorException(NOT_FOUND)
        }
        val fraudCheckService = FraudCheckService("http://localhost:8080/fraud/{customerId}", restTemplate, eventPublisher)

        fraudCheckService.isFraudulent("1").status shouldBe true
    }

    @Test
    fun `return fraud status as true if fraud provider fails to provide data`() {
        val restTemplate = mockk<RestTemplate> {
            every {
                getForEntity(
                    "http://localhost:8080/fraud/1",
                    FraudCheck::class.java
                )
            } throws HttpClientErrorException(INTERNAL_SERVER_ERROR)
        }
        val fraudCheckService = FraudCheckService("http://localhost:8080/fraud/{customerId}", restTemplate, eventPublisher)

        fraudCheckService.isFraudulent("1").status shouldBe true
    }

    @Test
    fun `raises event`() {
        var eventPublished = false
        val eventPublisher = ApplicationEventPublisher {
            eventPublished = true
        }
        val restTemplate = mockk<RestTemplate> {
            every {
                getForEntity(
                    "http://localhost:8080/fraud/1",
                    FraudCheck::class.java
                )
            } throws HttpClientErrorException(INTERNAL_SERVER_ERROR)
        }
        val fraudCheckService = FraudCheckService("http://localhost:8080/fraud/{customerId}", restTemplate, eventPublisher)

        fraudCheckService.isFraudulent("1")

        eventPublished shouldBe true
    }
}