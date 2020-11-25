package com.swapnilsankla.cdc.loangateway.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class BeanConfiguration {
	@Bean
	fun restTemplates() = RestTemplate()
}