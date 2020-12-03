package com.swapnilsankla.cdc

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.core.io.support.PropertiesLoaderUtils

class PactVerificationResultExtension: BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext?) {
        System.setProperty("pact.verifier.publishResults", "true")
        System.setProperty("pact.provider.version", getVersion())
    }

    private fun getVersion(): String {
        return PropertiesLoaderUtils
            .loadAllProperties("application.yaml")
            .getProperty("app.version")
    }
}