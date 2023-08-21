package edu.mirea.onebeattrue.services

import android.app.job.JobInfo
import android.content.Context
import android.content.ServiceConnection
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result { // метод выполняется в другом потоке
        log("doWork()")
        val page = workerParameters.inputData.getInt(PAGE, 0)
        for (i in 0..5) {
            Thread.sleep(1000)
            log("Timer: $i $page")
        }
        return Result.success() // есть еще retry и failure, думаю понятно что они вернут
    }

    private fun log(string: String) {
        Log.d("MyWorker", "MyWorker: $string")
    }

    companion object {
        private const val PAGE = "page"
        const val WORK_NAME = "work_name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>().apply {
                setInputData(workDataOf(PAGE to page)) // кладем аргумент
                setConstraints(makeConstraints())
            }.build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
        }
    }
}