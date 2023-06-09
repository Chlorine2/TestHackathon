/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally.data

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlinx.parcelize.Parcelize

/* Hard-coded data for the Rally sample. */




@Immutable
data class Account(
    val name: String,
    var balance: Float,
    val color: Color
)

@Immutable
data class Bill(
    val name: String,
    val due: String,
    var amount: Float,
    val color: Color
)

@Immutable
data class Deposit(
    val name: String,
    var amount: Float,
    val color: Color
)

@Immutable
@Parcelize
data class Credit(
    val name: String,
    var amount: Float,
    val color: Long
) : Parcelable

/**
 * Pretend repository for user's data.
 */
object UserData {
    var accounts: MutableList<Account> = mutableListOf(
        Account(
            "Checking",
            2215.13f,
            Color(0xFF004940)
        ),
        Account(
            "Home Savings",
            8676.88f,
            Color(0xFF005D57)
        ),
        Account(
            "Car Savings",
            987.48f,
            Color(0xFF04B97F)
        ),
        Account(
            "Vacation",
            253f,
            Color(0xFF37EFBA)
        )
    )
    var bills: MutableList<Bill> = mutableListOf(
        Bill(
            "RedPay Credit",
            "Jan 29",
            45.36f,
            Color(0xFFFFDC78)
        ),
        Bill(
            "Rent",
            "Feb 9",
            1200f,
            Color(0xFFFF6951)
        ),
        Bill(
            "TabFine Credit",
            "Feb 22",
            87.33f,
            Color(0xFFFFD7D0)
        ),
        Bill(
            "ABC Loans",
            "Feb 29",
            400f,
            Color(0xFFFFAC12)
        ),
        Bill(
            "ABC Loans 2",
            "Feb 29",
            77.4f,
            Color(0xFFFFAC12)
        )
    )

    var deposits: MutableList<Deposit> = mutableListOf(
        Deposit(
            "MonoBank",
            45.36f,
            Color(0xFF004940)
        ),
        Deposit(
            "Privat",
            1200f,
            Color(0xFF005D57)
        ),
        Deposit(
            "KredoBank",
            87.33f,
            Color(0xFF04B97F)
        ),
        Deposit(
            "OschadBank",
            400f,
            Color(0xFF37EFBA)
        ),
        Deposit(
            "LvivBank",
            77.4f,
            Color(0xFF01D52B)
        )
    )
    var credits: MutableList<Credit> = mutableListOf(
        Credit(
            "RedPay Credit",
            45.36f,
           0xFFFFDC78
        ),
        Credit(
            "Rent",
            1200f,
            0xFFFF6951
        ),
        Credit(
            "TabFine Credit",
            87.33f,
            0xFFFFD7D0
        ),
        Credit(
            "ABC Loans",
            400f,
            0xFFFFAC12
        ),
        Credit(
            "ABC Loans 2",
            77.4f,
            0xFFFFAC12
        )


    )


    fun getAccount(accountName: String?): Account {
        return accounts.first { it.name == accountName }
    }

   // fun addAccount(name: String, balance: Float, color: Color) {
   //     accounts.add(Account(name, balance, color))
   // }
//
   // fun addBill(name: String, due: String, balance: Float, color: Color) {
   //     this.bills.add(Bill(name, due, balance, color))
   // }
//
   // fun addDeposit(name: String,  balance: Float, color: Color) {
   //     this.deposits.add(Deposit(name, balance, color))
   // }
//
   // fun addCredit(name: String, balance: Float, color: Color) {
   //     this.credits.add(Credit(name, balance, color))
   // }

    fun getBill(billName: String?): Bill {
        return this.bills.first { it.name == billName }
    }
    fun getDeposit(depositName: String?): Deposit {
        return this.deposits.first { it.name == depositName }
    }
    fun getCredit(creditName: String?): Credit {
        return this.credits.first { it.name == creditName }
    }
}
