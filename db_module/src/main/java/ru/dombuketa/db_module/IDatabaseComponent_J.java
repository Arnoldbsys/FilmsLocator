package ru.dombuketa.db_module;

import javax.inject.Singleton;

import dagger.Component;
import ru.dombuketa.db_module.api.IAppProvider_J;
import ru.dombuketa.db_module.api.IDatabaseProvider_J;

@Singleton
@Component(dependencies = {IAppProvider_J.class},modules = {DatabaseModule_J.class})
public interface IDatabaseComponent_J extends IDatabaseProvider_J {
}
