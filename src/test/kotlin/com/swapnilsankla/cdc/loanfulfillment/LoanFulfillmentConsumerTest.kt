package com.swapnilsankla.cdc.loanfulfillment

import au.com.dius.pact.consumer.MessagePactBuilder
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.consumer.junit5.ProviderType
import au.com.dius.pact.core.model.annotations.Pact
import au.com.dius.pact.core.model.messaging.Message
import au.com.dius.pact.core.model.messaging.MessagePact
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.swapnilsankla.cdc.loanfulfillment.model.LoanApplication
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@ExtendWith(PactConsumerTestExt::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@PactTestFor(providerType = ProviderType.ASYNCH)
class LoanFulfillmentConsumerTest {
    @Pact(consumer = "loan_fulfillment_service", provider = "loan_gateway")
    fun eventForLoanFulfillment(builder: MessagePactBuilder): MessagePact {
        return builder
            .expectsToReceive("Loan creation event")
            .withMetadata(mapOf("traceId" to "1"))
            .withContent(PactDslJsonBody()
                .`object`("fraudCheck", PactDslJsonBody().booleanType("status"))
                .stringType("customerId"))
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "eventForLoanFulfillment")
    fun `should return false fraud status`(messages: List<Message>) {
        messages.size shouldBe 1
        jacksonObjectMapper().readValue(messages[0].contents.valueAsString(), LoanApplication::class.java)
    }
}