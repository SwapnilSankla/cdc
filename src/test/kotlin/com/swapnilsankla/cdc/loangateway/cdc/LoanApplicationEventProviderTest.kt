package com.swapnilsankla.cdc.loangateway.cdc

import au.com.dius.pact.provider.MessageAndMetadata
import au.com.dius.pact.provider.PactVerifyProvider
import au.com.dius.pact.provider.junit.Provider
import au.com.dius.pact.provider.junit.loader.PactFolder
import au.com.dius.pact.provider.junit5.AmpqTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.swapnilsankla.cdc.loangateway.model.FraudCheck
import com.swapnilsankla.cdc.loangateway.model.LoanApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@PactFolder("target/pacts")
@Provider("loan_gateway")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LoanApplicationEventProviderTest {
    @BeforeEach
    fun setup(context: PactVerificationContext) {
        context.target = AmpqTestTarget()
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun pactVerificationTestTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @PactVerifyProvider("Loan creation event")
    fun exampleEvent(): MessageAndMetadata {
        val eventString = jacksonObjectMapper().writeValueAsString(LoanApplication("1", FraudCheck(false)))
        return MessageAndMetadata(eventString.toByteArray(), mapOf("traceId" to "1"))
    }
}