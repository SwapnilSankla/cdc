package com.swapnilsankla.cdc.loangateway.event

import com.swapnilsankla.cdc.loangateway.model.LoanApplication
import org.springframework.context.ApplicationEvent

class LoanApplicationEvent(loanApplication: LoanApplication): ApplicationEvent(loanApplication)