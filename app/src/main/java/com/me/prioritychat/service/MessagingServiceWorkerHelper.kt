package com.me.prioritychat.service

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters


class MessagingServiceWorkerHelper(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val TAG = "MyWorker"

    override fun doWork(): Result {
        Log.d(TAG, "Performing long running task in scheduled job")
        // TODO add long running task here.
        return Result.success()
    }
}