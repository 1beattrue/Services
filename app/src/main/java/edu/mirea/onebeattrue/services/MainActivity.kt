package edu.mirea.onebeattrue.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import edu.mirea.onebeattrue.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var page = 0

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.simpleService.setOnClickListener {
            stopService(MyForegroundService.newIntent(this)) // остановка сервиса снаружи
            startService(MyService.newIntent(this, start = 10))
        }

        binding.foregroundService.setOnClickListener {
            startForegroundService(MyForegroundService.newIntent(this))
        }

        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService( // еще способ запуска сервиса
                this,
                MyIntentService.newIntent(this)
            )
        }

        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)

            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                .setRequiresCharging(true) // будет работать только на зарядке
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // будет работать только при подключении к wi-fi
                // .setPersisted(true) // запуск работы сервиса при перезагрузке устройства
                // .setPeriodic() // запуск с интервалом
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            val intent = MyJobService.newIntent(page++)
            jobScheduler.enqueue(jobInfo, JobWorkItem(intent)) // кладем запрос в очередь
        }

        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }

        binding.workManager.setOnClickListener {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND, // как вести себя, если запущено несколько сервисов
                MyWorker.makeRequest(page++)
            )
        }
    }
}
