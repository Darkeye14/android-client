/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.core.objects.account.loan

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Type(
    var id: Int? = null,

    var code: String? = null,

    var value: String? = null,

    var disbursement: Boolean? = null,

    var repaymentAtDisbursement: Boolean? = null,

    var repayment: Boolean? = null,

    var contra: Boolean? = null,

    var waiveInterest: Boolean? = null,

    var waiveCharges: Boolean? = null,

    var accrual: Boolean? = null,

    var writeOff: Boolean? = null,

    var recoveryRepayment: Boolean? = null,

    var initiateTransfer: Boolean? = null,

    var approveTransfer: Boolean? = null,

    var withdrawTransfer: Boolean? = null,

    var rejectTransfer: Boolean? = null,

    var chargePayment: Boolean? = null,

    var refund: Boolean? = null,
) : Parcelable