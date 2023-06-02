package ru.dombuketa.db_module

import dagger.Component
import ru.dombuketa.db_module.api.IAppProvider
import ru.dombuketa.db_module.api.IDatabaseProvider
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [IAppProvider::class],
    modules = [DatabaseModule::class]
)
interface IDatabaseComponent : IDatabaseProvider