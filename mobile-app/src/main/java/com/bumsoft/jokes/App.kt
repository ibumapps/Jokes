package com.bumsoft.jokes

import android.app.Application
import com.bumsoft.jokes.service.DataService
import com.bumsoft.jokes.service.repository.ServerRepository
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


/**
 * Created by yvshvets on 29.08.17.
 */
class App: Application() {

    var serverRepository: ServerRepository? = null
    var dataService: DataService? = null

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://104.155.96.154/jokes/")
                .build()

        serverRepository = retrofit.create<ServerRepository> (ServerRepository::class.java)
        dataService = DataService(serverRepository!!)
    }

    fun serverRepository(): ServerRepository? {

        return serverRepository
    }

    fun getServerService(): DataService {

        return dataService!!
    }
}