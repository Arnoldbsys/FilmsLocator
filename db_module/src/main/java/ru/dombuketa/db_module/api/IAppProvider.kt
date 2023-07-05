package ru.dombuketa.db_module.api

import android.content.Context

interface IAppProvider {
    fun provideContext() : Context
}