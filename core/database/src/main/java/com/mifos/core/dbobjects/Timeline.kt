/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.dbobjects

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Timeline")
data class Timeline(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "submittedOnDate")
    var submittedOnDate: List<Int> = ArrayList(),

    @ColumnInfo(name = "submittedByUsername")
    var submittedByUsername: String? = null,

    @ColumnInfo(name = "submittedByFirstname")
    var submittedByFirstname: String? = null,

    @ColumnInfo(name = "submittedByLastname")
    var submittedByLastname: String? = null,

    @ColumnInfo(name = "activatedOnDate")
    var activatedOnDate: List<Int> = ArrayList(),

    @ColumnInfo(name = "activatedByUsername")
    var activatedByUsername: String? = null,

    @ColumnInfo(name = "activatedByFirstname")
    var activatedByFirstname: String? = null,

    @ColumnInfo(name = "activatedByLastname")
    var activatedByLastname: String? = null,

    @ColumnInfo(name = "closedOnDate")
    var closedOnDate: List<Int> = ArrayList(),

    @ColumnInfo(name = "closedByUsername")
    var closedByUsername: String? = null,

    @ColumnInfo(name = "closedByFirstname")
    var closedByFirstname: String? = null,

    @ColumnInfo(name = "closedByLastname")
    var closedByLastname: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        submittedOnDate = parcel.createIntArray()?.toList() ?: emptyList(),
        submittedByUsername = parcel.readString(),
        submittedByFirstname = parcel.readString(),
        submittedByLastname = parcel.readString(),
        activatedOnDate = parcel.createIntArray()?.toList() ?: emptyList(),
        activatedByUsername = parcel.readString(),
        activatedByFirstname = parcel.readString(),
        activatedByLastname = parcel.readString(),
        closedOnDate = parcel.createIntArray()?.toList() ?: emptyList(),
        closedByUsername = parcel.readString(),
        closedByFirstname = parcel.readString(),
        closedByLastname = parcel.readString()
    )

    companion object : Parceler<Timeline> {

        override fun Timeline.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeIntArray(submittedOnDate.toIntArray())
            parcel.writeString(submittedByUsername)
            parcel.writeString(submittedByFirstname)
            parcel.writeString(submittedByLastname)
            parcel.writeIntArray(activatedOnDate.toIntArray())
            parcel.writeString(activatedByUsername)
            parcel.writeString(activatedByFirstname)
            parcel.writeString(activatedByLastname)
            parcel.writeIntArray(closedOnDate.toIntArray())
            parcel.writeString(closedByUsername)
            parcel.writeString(closedByFirstname)
            parcel.writeString(closedByLastname)
        }

        override fun create(parcel: Parcel): Timeline {
            return Timeline(parcel)
        }
    }
}
