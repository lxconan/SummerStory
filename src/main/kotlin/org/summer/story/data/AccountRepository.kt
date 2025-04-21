package org.summer.story.data

import org.ktorm.dsl.*
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.summer.story.data.models.AccountEntity
import org.summer.story.data.models.Accounts

class AccountRepository(private val dataSource: MapleDataSource) {
    fun findAccountByName(name: String): AccountEntity? {
        return dataSource.database.sequenceOf(Accounts)
            .filter { it.name eq name }
            .firstOrNull()
    }
}