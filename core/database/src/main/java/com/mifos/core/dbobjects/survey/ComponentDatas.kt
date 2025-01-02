/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.dbobjects.survey

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 28/3/16.
 */
@Parcelize
@Entity("ComponentDatas")
data class ComponentDatas(
    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo("key")
    var key: String? = null,

    @ColumnInfo("text")
    var text: String? = null,

    @ColumnInfo("description")
    var description: String? = null,

    @ColumnInfo("sequenceNo")
    var sequenceNo: Int = 0,
) : Parcelable
