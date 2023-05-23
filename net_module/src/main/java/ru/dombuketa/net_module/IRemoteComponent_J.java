package ru.dombuketa.net_module;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {RemoteModule_J.class})
public interface IRemoteComponent_J extends IRemoteProvider_J {
}
