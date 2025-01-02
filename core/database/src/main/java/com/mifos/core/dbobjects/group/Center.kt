/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.dbobjects.group

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mifos.core.dbobjects.Timeline
import com.mifos.core.dbobjects.client.Status
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Center")
data class Center(
    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo(name = "sync")
    @Transient
    var sync: Boolean = false,

    @ColumnInfo(name = "accountNo")
    var accountNo: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "officeId")
    var officeId: Int? = null,

    @ColumnInfo(name = "officeName")
    var officeName: String? = null,

    @ColumnInfo(name = "staffId")
    var staffId: Int? = null,

    @ColumnInfo(name = "staffName")
    var staffName: String? = null,

    @ColumnInfo(name = "hierarchy")
    var hierarchy: String? = null,

    var status: Status? = null,

    @ColumnInfo(name = "active")
    var active: Boolean? = null,

    @Embedded
    @Transient
    var centerDate: CenterDate? = null,

    var activationDate: List<Int?> = ArrayList(),

    var timeline: Timeline? = null,

    var externalId: String? = null,
) : Parcelable