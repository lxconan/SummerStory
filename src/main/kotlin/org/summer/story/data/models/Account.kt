package org.summer.story.data.models

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar

object Account : Table<Nothing>("accounts") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val password = varchar("password")
    val hardwareId = varchar("hardware_id")
    val createdAt = timestamp("created_at")
}