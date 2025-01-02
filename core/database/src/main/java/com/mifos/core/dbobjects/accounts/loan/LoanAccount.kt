/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.dbobjects.accounts.loan

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "LoanAccount")
data class LoanAccount(
    @ColumnInfo(name = "clientId")
    var clientId: Long = 0,

    @ColumnInfo(name = "groupId")
    var groupId: Long = 0,

    @ColumnInfo(name = "centerId")
    var centerId: Long = 0,

    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo(name = "accountNo")
    var accountNo: String? = null,

    @ColumnInfo(name = "externalId")
    var externalId: String? = null,

    @ColumnInfo(name = "productId")
    var productId: Int? = null,

    @ColumnInfo(name = "productName")
    var productName: String? = null,

    @Embedded // For embedding Status object
    var status: Status? = null,

    @Embedded // For embedding LoanType object
    var loanType: LoanType? = null,

    @ColumnInfo(name = "loanCycle")
    var loanCycle: Int? = null,

    @ColumnInfo(name = "inArrears")
    var inArrears: Boolean? = null,
) : Parcelable {

    fun isInArrears(): Boolean? {
        return inArrears
    }
}
