package com.example.todoapp.todolist.framework

import android.app.Application
import com.example.todoapp.todolist.framework.injection.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TodoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TodoApplication)
            modules(appModule)
        }
    }
}