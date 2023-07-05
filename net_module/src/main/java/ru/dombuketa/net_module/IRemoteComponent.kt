package ru.dombuketa.net_module

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RemoteModule::class])
interface IRemoteComponent : IRemoteProvider
