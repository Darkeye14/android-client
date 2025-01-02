/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.dbobjects.client

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 05/07/16.
 */
@Parcelize
@Entity(tableName = "ChargeTimeType")
data class ChargeTimeType(
    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo("Code")
    var code: String? = null,

    @ColumnInfo("value")
    var value: String? = null,
) : Parcelable
