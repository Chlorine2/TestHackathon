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

package com.example.compose.rally.ui.accounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.compose.rally.R
import com.example.compose.rally.data.Account
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.components.AccountRow
import com.example.compose.rally.ui.components.ChangeAmount
import com.example.compose.rally.ui.components.StatementBody

/**
 * The Accounts screen.
 */


enum class SortingOption {
    NAME_ASCENDING, NAME_DESCENDING, PRICE_ASCENDING, PRICE_DESCENDING
}

@Composable
fun AccountsScreen(
     onAccountClick: (String) -> Unit = {},
) {
    val amountsTotal = remember { UserData.accounts.map { account -> account.balance }.sum() }

    var selectedSortingOption by remember { mutableStateOf(SortingOption.NAME_ASCENDING) }
    val filteredAndSortedAccounts = remember(selectedSortingOption) {
        val sortedAccounts = when (selectedSortingOption) {
            SortingOption.NAME_ASCENDING -> UserData.accounts.sortedBy { it.name }
            SortingOption.NAME_DESCENDING -> UserData.accounts.sortedByDescending { it.name }
            SortingOption.PRICE_ASCENDING -> UserData.accounts.sortedBy { it.balance }
            SortingOption.PRICE_DESCENDING -> UserData.accounts.sortedByDescending { it.balance }
        }
        sortedAccounts
    }

    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.padding(top = 16.dp)) {
        Row(Modifier.padding(bottom = 8.dp)) {
            Text(
                "Sort by:",
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = { expanded = !expanded },
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = when (selectedSortingOption) {
                        SortingOption.NAME_ASCENDING -> "Name (A-Z)"
                        SortingOption.NAME_DESCENDING -> "Name (Z-A)"
                        SortingOption.PRICE_ASCENDING -> "Price (Low-High)"
                        SortingOption.PRICE_DESCENDING -> "Price (High-Low)"
                    },
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        }

        if (expanded) {
            DropdownMenuContainer(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOption.NAME_ASCENDING
                    expanded = false
                }) {
                    Text("Name (A-Z)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOption.NAME_DESCENDING
                    expanded = false
                }) {
                    Text("Name (Z-A)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOption.PRICE_ASCENDING
                    expanded = false
                }) {
                    Text("Price (Low-High)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOption.PRICE_DESCENDING
                    expanded = false
                }) {
                    Text("Price (High-Low)")
                }
            }
        }

        StatementBody(
            modifier=Modifier.fillMaxWidth(),
            items=filteredAndSortedAccounts,
            amountsTotal=amountsTotal,
            amounts={account->account.balance},
            colors={account->account.color},
            circleLabel=stringResource(R.string.total),
            rows={account->
                AccountRow(
                    modifier=Modifier.clickable{
                        onAccountClick(account.name)
                    },
                    name=account.name,
                    amount=account.balance,
                    color=account.color
                )
            }
        )
    }
}

@Composable
fun DropdownMenuContainer(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    if (expanded) {
        Box(
            modifier=Modifier.fillMaxSize().clickable(onClick=onDismissRequest),
            contentAlignment=Alignment.TopEnd
        ) {
            Column(content=content)
        }
    }
}



/**
 * Detail screen for a single account.
 */
@Composable
fun SingleAccountScreen(
    accountType: String? = UserData.accounts.first().name,
    navController : NavHostController
) {
    val account = remember(accountType) { UserData.getAccount(accountType) }

    val deleteAccount: () -> Unit = {
        UserData.accounts.remove(account)
        navController.popBackStack()
    }

    var showDialog by remember { mutableStateOf(false) }
    val openDialog: () -> Unit = {
        showDialog = true
    }
    val closeDialog: () -> Unit = {
        showDialog = false
    }
    if (showDialog) {
        ChangeAmount(
            item = account,
            amountGetter = Account::balance,
            amountSetter = { item, amount ->
                item.balance = amount
            },
            onCloseDialog = closeDialog
        )
    }

    StatementBody(
        items = listOf(account),
        colors = { account.color },
        amounts = { account.balance },
        amountsTotal = account.balance,
        circleLabel = account.name,
    ) { row ->
        Column() {
            AccountRow(
                name = row.name,
                amount = row.balance,
                color = row.color
            )
            Button(
                onClick = openDialog, shape = RoundedCornerShape(100.dp),
                modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth().requiredHeight(65.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface,),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(text = "Edit")
            }

            Button(
                onClick = deleteAccount, shape = RoundedCornerShape(100.dp),
                modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth().requiredHeight(65.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB82100)),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(text = "Delete", color = Color.White)
            }
        }
    }
}
