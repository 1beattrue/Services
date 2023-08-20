package edu.mirea.onebeattrue.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService : JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartJob(params: JobParameters?): Boolean { // выполняется на главном потоке (возвращаемый тип - работа все еще выполняется?)
        log("onStartJob")
        coroutineScope.launch {
            var workItem = params?.dequeueWork() // достаем запрос из очереди
            while (workItem != null) {
                val page = workItem.intent?.getIntExtra(PAGE, 0)

                for (i in 1 until 5) {
                    delay(1000)
                    log("Timer $i $page")
                }

                params?.completeWork(workItem)
                workItem = params?.dequeueWork()
            }
            jobFinished(params, false)
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
        coroutineScope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("MyJobService", "MyJobService: $message")
    }

    companion object {
        const val JOB_ID = 228
        private const val PAGE = "page"

        fun newIntent(page: Int): Intent { // как Bundle, только для примитивов и строк (чтобы можно было считывать с диска)
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }
}