package org.summer.story.server.login.game

import org.slf4j.LoggerFactory
import org.summer.story.server.login.LoginReceiveOpcode

abstract class GameProcessorFactory(processors: List<GameProcessor>) {
    private val processorMap: Map<Short, GameProcessor>

    init {
        val internalProcessors = mutableMapOf<Short, GameProcessor>()
        processors.forEach { processor ->
            registerHandler(internalProcessors, processor.getOpcode(), processor)
        }

        processorMap = internalProcessors.toMap()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GameProcessorFactory::class.java)

        private fun registerHandler(
            processors: MutableMap<Short, GameProcessor>,
            opcode: LoginReceiveOpcode,
            processor: GameProcessor
        ) {
            if (processors.containsKey(opcode.value.toShort())) {
                // just a warning, we can overwrite the processor
                logger.warn("Processor for opcode {} already exists. Overwriting.", opcode)
            }

            processors[opcode.value.toShort()] = processor
        }
    }

    fun getGameProcessor(opcode: Short): GameProcessor? {
        val gameProcessor: GameProcessor? = processorMap[opcode]

        if (gameProcessor == null) {
            logger.warn("No processor found for opcode {}", opcode)
        }

        return gameProcessor
    }
}

class LoginGameProcessorFactory(processors: List<LoginServerGameProcessor>) : GameProcessorFactory(processors)