/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mifos.core.network.datamanager.DataManagerClient
import com.mifos.core.objects.client.Client
import com.mifos.core.objects.client.Page
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Aditya Gupta on 21/02/24.
 */

class ClientListPagingSource(
    private val dataManagerClient: DataManagerClient,
) : PagingSource<Int, Client>() {

    override fun getRefreshKey(state: PagingState<Int, Client>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(10) ?: state.closestPageToPosition(
                position,
            )?.nextKey?.minus(10)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Client> {
        val position = params.key ?: 0
        return try {
            val getClients = getClientList(position)
            val clientList = getClients.first
            val totalClients = getClients.second
            val clientDbList = getClientDbList()
            val clientListWithSync = getClientListWithSync(clientList, clientDbList)
            LoadResult.Page(
                data = clientListWithSync,
                prevKey = if (position <= 0) null else position - 10,
                nextKey = if (position >= totalClients) null else position + 10,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun getClientList(position: Int): Pair<List<Client>, Int> {
        val response = dataManagerClient.getAllClients(position, 10)
        return Pair(response.pageItems, response.totalFilteredRecords)
    }

    private suspend fun getClientDbList(): List<Client> {
        return suspendCoroutine { continuation ->
            dataManagerClient.allDatabaseClients
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Page<Client>>() {
                    override fun onCompleted() {
                    }

                    override fun onError(error: Throwable) {
                        continuation.resumeWithException(error)
                    }

                    override fun onNext(clients: Page<Client>) {
                        continuation.resume(clients.pageItems)
                    }
                })
        }
    }

    private fun getClientListWithSync(
        clientList: List<Client>,
        clientDbList: List<Client>,
    ): List<Client> {
        if (clientDbList.isNotEmpty()) {
            clientList.forEach { client ->
                clientDbList.forEach { clientDb ->
                    if (client.id == clientDb.id) {
                        client.sync = true
                    }
                }
            }
        }
        return clientList
    }
}
