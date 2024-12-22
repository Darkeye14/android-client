/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.objects.noncore

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mifos.core.database.MifosDatabase
import com.mifos.core.model.MifosBaseModel

import kotlinx.parcelize.Parcelize

/**
 * Created by ishankhanna on 16/06/14.
 */
@Parcelize
@Entity("ColumnValue")
data class ColumnValue(
    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo("value")
    var value: String? = null,

    @ColumnInfo("score")
    var score: Int? = null,

    @ColumnInfo("registeredTableName")
    var registeredTableName: String? = null,
) : MifosBaseModel(), Parcelable
