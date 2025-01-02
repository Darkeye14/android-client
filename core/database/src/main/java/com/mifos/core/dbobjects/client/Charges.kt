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
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

/**
 * Created by nellyk on 2/15/2016.
 */
/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
@Parcelize
@Entity(tableName = "Charges")
data class Charges(
    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo(name = "clientId")
    var clientId: Int? = null,

    @ColumnInfo(name = "loanId")
    var loanId: Int? = null,

    @ColumnInfo(name = "chargeId")
    var chargeId: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @Embedded
    var chargeTimeType: ChargeTimeType? = null,

    @Embedded
    var chargeDueDate: ClientDate? = null,

    var dueDate: List<Int> = ArrayList(),

    @Embedded
    var chargeCalculationType: ChargeCalculationType? = null,

    @Embedded
    var currency: Currency? = null,

    @ColumnInfo(name = "amount")
    var amount: Double? = null,

    @ColumnInfo(name = "amountPaid")
    var amountPaid: Double? = null,

    @ColumnInfo(name = "amountWaived")
    var amountWaived: Double? = null,

    @ColumnInfo(name = "amountWrittenOff")
    var amountWrittenOff: Double? = null,

    @ColumnInfo(name = "amountOutstanding")
    var amountOutstanding: Double? = null,

    @ColumnInfo(name = "penalty")
    var penalty: Boolean? = null,

    @ColumnInfo(name = "active")
    var active: Boolean? = null,

    @ColumnInfo(name = "paid")
    var paid: Boolean? = null,

    @ColumnInfo(name = "waived")
    var waived: Boolean? = null,
) : Parcelable {

    val formattedDueDate: String
        get() {
            val pattern = "%s-%s-%s"
            if (dueDate.size > 2) {
                return String.format(
                    pattern,
                    dueDate[0],
                    dueDate[1],
                    dueDate[2],
                )
            }
            return "No Due Date"
        }
}
