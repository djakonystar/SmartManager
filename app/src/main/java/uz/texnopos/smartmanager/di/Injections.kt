package uz.texnopos.smartmanager.di

import com.google.gson.GsonBuilder
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.texnopos.smartmanager.data.remote.ApiInterface
import uz.texnopos.smartmanager.settings.Settings
import uz.texnopos.smartmanager.ui.admin.AdminAdapter
import uz.texnopos.smartmanager.ui.admin.AdminViewModel
import uz.texnopos.smartmanager.ui.report.ReportAdapter
import uz.texnopos.smartmanager.ui.report.ReportViewModel
import uz.texnopos.smartmanager.ui.settings.SettingsViewModel
import uz.texnopos.smartmanager.ui.signin.SignInViewModel
import java.util.concurrent.TimeUnit

private const val baseUrl = "http://199.19.72.116:3000/"
private const val timeOut = 20L

val networkModule = module {
    single {
        GsonBuilder().setLenient().create()
    }

    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(ApiInterface::class.java)
    }
}

val helperModule = module {
    singleOf(::Settings)
}

val viewModelModule = module {
    viewModelOf(::SignInViewModel)
    viewModelOf(::ReportViewModel)
    viewModelOf(::AdminViewModel)
    viewModelOf(::SettingsViewModel)
}

val adapterModule = module {
    singleOf(::ReportAdapter)
    singleOf(::AdminAdapter)
}
