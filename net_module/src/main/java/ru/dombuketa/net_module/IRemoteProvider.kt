package ru.dombuketa.net_module

interface IRemoteProvider {
    fun provideRemote(): ITmdbApi
}