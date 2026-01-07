package com.apptive.japkor.data.repository

import com.apptive.japkor.data.api.ServiceFactory
import com.apptive.japkor.data.model.MyStatusResponse

class StatusRepository {
    suspend fun getMyStatus(): MyStatusResponse =
        ServiceFactory.matchingService.getMyStatus()
}
