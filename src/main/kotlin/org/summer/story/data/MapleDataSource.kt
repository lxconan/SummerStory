package org.summer.story.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.summer.story.config.GlobalConfiguration
import java.sql.Connection
import javax.sql.DataSource

interface MapleDataSource {
    fun getConnection(): Connection
}

class MapleDataSourceImpl(private val configuration: GlobalConfiguration) : MapleDataSource {
    private val dataSource: DataSource

    init {
        val dataSourceConfig = HikariConfig().apply {
            jdbcUrl = configuration.database.jdbcUrl
            username = configuration.database.username
            password = configuration.database.password
            maximumPoolSize = configuration.database.maximumPoolSize
        }

        dataSource = HikariDataSource(dataSourceConfig)
    }

    override fun getConnection(): Connection {
        return dataSource.connection
    }
}