package com.ibeybeh.beylearn.navigation.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.ibeybeh.beylearn.navigation.processor.processor.BeyNavigationProcessor

class BeyNavigationProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return BeyNavigationProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}