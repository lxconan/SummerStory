package org.summer.story.config

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream

inline fun<reified T> loadEmbeddedResource(path: String, defaultInstanceFunc: () -> T): T {
    GlobalConfiguration::class.java.getResourceAsStream(path).use { inputStream ->
        when (inputStream) {
            null -> return defaultInstanceFunc()
            else -> return Yaml.default.decodeFromStream(inputStream)
        }
    }
}