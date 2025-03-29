package org.summer.story.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import org.summer.story.config.GlobalConfiguration
import javax.sql.DataSource

interface MapleDataSource {
    val database: Database
}

class MapleDataSourceImpl(private val configuration: GlobalConfiguration) : MapleDataSource {
    private val dataSource: DataSource
    override val database: Database

    init {
        val dataSourceConfig = HikariConfig().apply {
            jdbcUrl = configuration.database.jdbcUrl
            username = configuration.database.username
            password = configuration.database.password
            maximumPoolSize = configuration.database.maximumPoolSize
        }

        dataSource = HikariDataSource(dataSourceConfig)
        database = Database.connect(dataSource)
    }
}