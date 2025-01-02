/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.dbobjects.organisation

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 7/7/16.
 */
@Parcelize
@Entity("OfficeOpeningDate")
data class OfficeOpeningDate(
    @PrimaryKey
    var officeId: Int? = null,

    @ColumnInfo("year")
    var year: Int? = null,

    @ColumnInfo("month")
    var month: Int? = null,

    @ColumnInfo("day")
    var day: Int? = null,
) : Parcelable
