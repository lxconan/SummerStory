package org.summer.story

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.ktorm.database.Database
import org.ktorm.dsl.insertAndGenerateKey
import org.summer.story.config.GlobalConfiguration
import org.summer.story.data.MapleDataSource
import org.summer.story.data.models.Account
import org.summer.story.net.encryption.HashAlgorithm

abstract class DatabaseEnabledTest {
    protected lateinit var koin: Koin
    protected lateinit var database: Database
    protected lateinit var configuration: GlobalConfiguration

    @BeforeEach
    fun databaseSetup() {
        val koinApplication = koinApplication {
            modules(ModuleFactory.createServerModule())
        }

        updateKoinModules(koinApplication)

        koin = koinApplication.koin

        val configuration = koin.get<GlobalConfiguration>()
        val flyway = Flyway.configure()
            .dataSource(
                configuration.database.jdbcUrl,
                configuration.database.username,
                configuration.database.password
            )
            .locations("classpath:db/migration")
            .cleanDisabled(false)
            .load()
        flyway.clean()
        flyway.migrate()

        database = koin.get<MapleDataSource>().database
        this.configuration = configuration
    }

    protected fun givenAccount(accountName: String, password: String, hardwareId: String = "00010203"): Int {
        val algorithm = koin.get<HashAlgorithm>()
        return database.insertAndGenerateKey(Account) {
            set(it.name, accountName)
            set(it.password, algorithm.sha256(password))
            set(it.hardwareId, hardwareId)
        } as Int
    }

    protected fun updateKoinModules(koinApplication: KoinApplication) { }
}