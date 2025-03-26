package org.summer.story.server.game

import org.slf4j.LoggerFactory
import org.summer.story.server.ReceiveOpcode

class GameProcessorFactory {
    private val processors: Map<Short, GameProcessor>

    init {
        val internalProcessors = mutableMapOf<Short, GameProcessor>()
        registerHandler(internalProcessors, ReceiveOpcode.PONG, KeepAliveProcessor())
        processors = internalProcessors.toMap()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GameProcessorFactory::class.java)

        private fun registerHandler(
            processors: MutableMap<Short, GameProcessor>,
            opcode: ReceiveOpcode,
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
        return if (processors.containsKey(opcode)) {
            processors[opcode]
        } else null
    }
}