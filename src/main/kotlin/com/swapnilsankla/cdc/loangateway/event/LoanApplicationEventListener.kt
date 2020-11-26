package com.swapnilsankla.cdc.loangateway.event

import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class LoanApplicationEventListener: ApplicationListener<LoanApplicationEvent> {
    override fun onApplicationEvent(event: LoanApplicationEvent) {
        // raise event
    }
}