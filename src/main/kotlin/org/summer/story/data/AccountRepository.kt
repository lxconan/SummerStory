package org.summer.story.data

import org.ktorm.dsl.*
import org.summer.story.data.models.Account

class AccountRepository(private val dataSource: MapleDataSource) {
    fun findAccountByName(name: String): AccountEntity? {
        return dataSource.database.from(Account)
            .select(Account.id, Account.name, Account.password, Account.hardwareId)
            .where { Account.name eq name }
            .map { row ->
                AccountEntity(
                    id = row[Account.id]!!,
                    name = row[Account.name]!!,
                    password = row[Account.password]!!,
                    hardwareId = row[Account.hardwareId]!!
                )
            }
            .firstOrNull()
    }
}

class AccountEntity(
    val id: Int,
    val name: String,
    val password: String,
    val hardwareId: String
)