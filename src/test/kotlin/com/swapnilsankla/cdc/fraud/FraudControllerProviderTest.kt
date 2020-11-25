package com.swapnilsankla.cdc.fraud

import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.State
import au.com.dius.pact.provider.junit.loader.PactFolder
import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import com.swapnilsankla.cdc.fraud.model.CustomerNotFoundException
import com.swapnilsankla.cdc.fraud.model.FraudCheck
import com.swapnilsankla.cdc.fraud.service.FraudService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(FraudControllerProviderTest.TestConfiguration::class)
@PactFolder("target/pacts")
@Provider("fraud_service")
class FraudControllerProviderTest {
    @LocalServerPort
    private val port = 0

    @BeforeEach
    fun before(context: PactVerificationContext) {
        context.target = HttpTestTarget("localhost", port)
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun pactVerificationTestTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State(
        value =
        [
            "Customer with id 1 is setup to return false fraudulent status",
            "Customer with id 2 is setup to return false fraudulent status",
            "Customer with id 3 is not setup"
        ]
    )
    fun setupIfAny() {
        // Nothing to setup as it is taken care by the mock FraudService
    }

    @org.springframework.boot.test.context.TestConfiguration
    class TestConfiguration {
        @Bean
        fun fraudService(): FraudService {
            return mockk {
                every { fraudStatus("1") } returns FraudCheck(false)
                every { fraudStatus("2") } returns FraudCheck(true)
                every { fraudStatus("3") } throws CustomerNotFoundException()
            }
        }
    }
}