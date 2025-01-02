/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.dbobjects.accounts.savings

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mifos.core.model.APIEndPoint
import kotlinx.parcelize.Parcelize

/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
@Parcelize
@Entity("SavingAccountDepositType")
class DepositType(
    @PrimaryKey
    var id: Int? = null,

    @ColumnInfo("code")
    var code: String? = null,

    @ColumnInfo("value")
    var value: String? = null,
) : Parcelable {

    val isRecurring: Boolean
        get() = ServerTypes.RECURRING.id == id
    val endpoint: String
        get() = ServerTypes.fromId(id!!).endpoint
    val serverType: ServerTypes
        get() = ServerTypes.fromId(id!!)

    enum class ServerTypes(val id: Int, val code: String, val endpoint: String) {
        // TODO: Are these all the types?
        SAVINGS(100, "depositAccountType.savingsDeposit", APIEndPoint.SAVINGS_ACCOUNTS),
        FIXED(
            200,
            "depositAccountType.fixedDeposit",
            APIEndPoint.SAVINGS_ACCOUNTS,
        ),
        RECURRING(300, "depositAccountType.recurringDeposit", APIEndPoint.RECURRING_ACCOUNTS),
        ;

        companion object {
            fun fromId(id: Int): ServerTypes {
                for (type in values()) {
                    if (type.id == id) {
                        return type
                    }
                }
                return SAVINGS
            }
        }
    }
}
