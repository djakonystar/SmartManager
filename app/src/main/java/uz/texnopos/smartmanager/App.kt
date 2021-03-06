package uz.texnopos.smartmanager

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.texnopos.smartmanager.di.adapterModule
import uz.texnopos.smartmanager.di.helperModule
import uz.texnopos.smartmanager.di.networkModule
import uz.texnopos.smartmanager.di.viewModelModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val modules = listOf(networkModule, helperModule, viewModelModule, adapterModule)

        startKoin {
            androidLogger()
            androidContext(this@App)
            androidFileProperties()
            koin.loadModules(modules)
        }
    }
}
