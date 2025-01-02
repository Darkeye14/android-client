/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.room.di

import android.content.Context
import com.mifos.room.dao.ColumnValueDao
import com.mifos.room.db.MifosDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object DbModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object DatabaseModule {

        @Provides
        @Singleton
        fun providesDatabase(@ApplicationContext context: Context): MifosDatabase {
            return MifosDatabase.getDatabase(context)
        }

        @Provides
        @Singleton
        fun providesColumnValueDao(database: MifosDatabase): ColumnValueDao {
            return database.columnValueDao()
        }
    }
}
