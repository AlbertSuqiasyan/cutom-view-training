//package com.example.customdraw.koin
//
//import android.app.Application
//import org.koin.android.ext.koin.androidContext
//import org.koin.core.context.startKoin
//
//class CustomViewApp : Application(){
//    override fun onCreate() {
//        super.onCreate()
//        startKoin {
//            androidContext(this@CustomViewApp)
//            modules(listOf(appModules))
//        }
//    }
//}