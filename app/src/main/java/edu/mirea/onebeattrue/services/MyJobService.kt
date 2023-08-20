package edu.mirea.onebeattrue.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService: JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartJob(params: JobParameters?): Boolean { // выполняется на главном потоке (возвращаемый тип - работа все еще выполняется?)
        log("onStartJob")
        coroutineScope.launch {
            for (i in 1..25) {
                delay(1000)
                log("Timer: $i")
            }
            jobFinished(params, true)
        }
        return true // по скольку мы делаем асинхронную работу мы сами завершим работу сервиса
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        // вызовется, если не выполняются ограничения(подключение к зарядке, подключение к wi-fi)
        log("onStopJob")
        return true // если хотим перезапустить сервис после того как система его убила
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        coroutineScope.cancel()
    }

    fun log(message: String) {
        Log.d("MyJobService", "MyJobService: $message")
    }
}