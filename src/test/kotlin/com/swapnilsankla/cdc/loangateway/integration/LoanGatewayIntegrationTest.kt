package com.swapnilsankla.cdc.loangateway.integration

import io.kotlintest.shouldBe
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LoanGatewayIntegrationTest {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Value("\${loan-gateway.fraud-service-port}")
    private var fraudServerPort: Int = 0

    private val mockServer = MockWebServer()

    @BeforeEach
    fun setup() {
        mockServer.start(fraudServerPort)
        val mockResponse = MockResponse()
        mockResponse
            .setBody("""{ "status": "false"}""")
            .setHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        mockServer.enqueue(mockResponse)
    }

    @Test
    fun `Loan gateway returns you will get it`() {
        val result: ResponseEntity<String> = restTemplate
            .getForEntity("/loans/apply/1", String::class.java)

        result.statusCode shouldBe HttpStatus.OK
        result.body shouldBe "You will get it"
    }
}