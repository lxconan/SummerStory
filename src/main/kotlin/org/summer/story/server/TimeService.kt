package org.summer.story.server

interface TimeService {
    fun currentTimeMillis(): Long
}

class TimeServiceImpl : TimeService {
    override fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}