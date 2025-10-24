package com.example.metrolima

import android.app.Application
import androidx.room.Room
import com.example.metrolima.data.MetroRepository
import com.example.metrolima.data.MetroRepositoryImpl
import com.example.metrolima.data.local.MetroDatabase
import com.example.metrolima.data.remote.MetroAlertsApi
import com.example.metrolima.data.remote.MockAlertsInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MetroApplication : Application() {
    lateinit var container: MetroAppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = MetroAppContainer(this)
    }
}

class MetroAppContainer(application: Application) {
    private val database: MetroDatabase = Room.databaseBuilder(
        application,
        MetroDatabase::class.java,
        "metro-lima-go.db"
    ).fallbackToDestructiveMigration().build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(MockAlertsInterceptor(application))
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://metrolimago.mock/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val alertsApi: MetroAlertsApi = retrofit.create(MetroAlertsApi::class.java)

    val repository: MetroRepository = MetroRepositoryImpl(
        stationDao = database.stationDao(),
        alertsApi = alertsApi
    )
}
