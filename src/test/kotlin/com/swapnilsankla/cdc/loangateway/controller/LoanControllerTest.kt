package com.swapnilsankla.cdc.loangateway.controller

import com.swapnilsankla.cdc.loangateway.model.FraudCheck
import com.swapnilsankla.cdc.loangateway.service.FraudCheckService
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(LoanController::class)
@Import(LoanControllerTest.TestConfiguration::class)
class LoanControllerTest {
    @Autowired
    private lateinit var client: MockMvc

    @Test
    fun `Loan controller returns You will get it message if no fraud found`() {
        val response = client
            .perform(get("/loans/apply/1"))
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        response shouldBe "You will get it"
    }

    @Test
    fun `Loan controller returns We are sorry if fraud is found`() {
        val response = client
            .perform(get("/loans/apply/2"))
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        response shouldBe "We are sorry!"
    }

    @org.springframework.boot.test.context.TestConfiguration
    class TestConfiguration {
        @Bean
        fun fraudCheckService() = mockk<FraudCheckService> {
            every { isFraudulent("1") } returns FraudCheck(false)
            every { isFraudulent("2") } returns FraudCheck(true)
        }
    }
}