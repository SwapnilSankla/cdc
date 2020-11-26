package com.swapnilsankla.cdc.loangateway.cdc

import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.swapnilsankla.cdc.loangateway.service.FraudCheckService
import io.kotlintest.shouldBe
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(port = "1234")
class FraudCheckServiceConsumerTest {

    @Autowired
    private lateinit var fraudCheckService: FraudCheckService

    @Pact(provider = "fraud_service", consumer = "loan_gateway")
    fun nonFraudulentCustomer(pactDsl: PactDslWithProvider): RequestResponsePact {
        return pactDsl
            .given("Customer with id 1 is setup to return false fraudulent status")
            .uponReceiving("When fraud status is requested for nonFraudulent customer")
            .method("GET")
            .path("/fraud/1")
            .willRespondWith()
            .status(200)
            .body(JSONObject("""{ "status": false }"""))
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "nonFraudulentCustomer")
    fun `should return fraud details with status as false for customerId 1`() {
        fraudCheckService.isFraudulent("1").status shouldBe false
    }

    @Pact(provider = "fraud_service", consumer = "loan_gateway")
    fun fraudulentCustomer(pactDsl: PactDslWithProvider): RequestResponsePact {
        return pactDsl
            .given("Customer with id 2 is setup to return true fraudulent status")
            .uponReceiving("When fraud status is requested for fraudulent customer")
            .method("GET")
            .path("/fraud/2")
            .willRespondWith()
            .status(200)
            .body(JSONObject("""{ "status": true }"""))
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "fraudulentCustomer")
    fun `should return fraud details with status as true for customerId 2`() {
        fraudCheckService.isFraudulent("2").status shouldBe true
    }

    @Pact(provider = "fraud_service", consumer = "loan_gateway")
    fun customerNotFound(pactDsl: PactDslWithProvider): RequestResponsePact {
        return pactDsl
            .given("Customer with id 3 is not setup")
            .uponReceiving("When fraud status is requested for non-existing customer")
            .method("GET")
            .path("/fraud/3")
            .willRespondWith()
            .status(404)
            .toPact()
    }

    @Test
    @PactTestFor(pactMethod = "customerNotFound")
    fun `should return 404 for non existing customerId`() {
        fraudCheckService.isFraudulent("3").status shouldBe true
    }
}