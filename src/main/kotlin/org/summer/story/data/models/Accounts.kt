package org.summer.story.data.models

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

interface AccountEntity : Entity<AccountEntity> {
    val id: Int
    val name: String
    val password: String
    val hardwareId: String
}

object Accounts : Table<AccountEntity>("accounts") {
    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val password = varchar("password").bindTo { it.password }
    val hardwareId = varchar("hardware_id").bindTo { it.hardwareId }
}
