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

package com.example.compose.rally.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.rally.data.Bill
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.accounts.DropdownMenuContainer
import com.example.compose.rally.ui.bills.SortingOptionBill

/**
 * Generic component used by the accounts and bills screens to show a chart and a list of items.
 */

@Composable
fun sorting() : List<Bill>{
    var selectedSortingOption by remember { mutableStateOf(SortingOptionBill.NAME_ASCENDING) }
    val filteredAndSortedBills = remember(selectedSortingOption) {
        val sortedBills = when (selectedSortingOption) {
            SortingOptionBill.NAME_ASCENDING -> UserData.bills.sortedBy { it.name }
            SortingOptionBill.NAME_DESCENDING -> UserData.bills.sortedByDescending { it.name }
            SortingOptionBill.AMOUNT_ASCENDING -> UserData.bills.sortedBy { it.amount }
            SortingOptionBill.AMOUNT_DESCENDING -> UserData.bills.sortedByDescending { it.amount }
        }
        sortedBills
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
                        SortingOptionBill.NAME_ASCENDING -> "Name (A-Z)"
                        SortingOptionBill.NAME_DESCENDING -> "Name (Z-A)"
                        SortingOptionBill.AMOUNT_ASCENDING -> "Amount (Low-High)"
                        SortingOptionBill.AMOUNT_DESCENDING -> "Amount (High-Low)"
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
                    selectedSortingOption = SortingOptionBill.NAME_ASCENDING
                    expanded = false
                }) {
                    Text("Name (A-Z)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOptionBill.NAME_DESCENDING
                    expanded = false
                }) {
                    Text("Name (Z-A)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOptionBill.AMOUNT_ASCENDING
                    expanded = false
                }) {
                    Text("Amount (Low-High)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOptionBill.AMOUNT_DESCENDING
                    expanded = false
                }) {
                    Text("Amount (High-Low)")
                }
            }
        }
    }
    return filteredAndSortedBills

}
@Composable
fun <T> StatementBody(
    modifier: Modifier = Modifier,
    items: List<T>,
    colors: (T) -> Color,
    amounts: (T) -> Float,
    amountsTotal: Float,
    circleLabel: String,
    rows: @Composable (T) -> Unit
) {

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Box(Modifier.padding(16.dp)) {
            val accountsProportion = items.extractProportions { amounts(it) }
            val circleColors = items.map { colors(it) }
            AnimatedCircle(
                accountsProportion,
                circleColors,
                Modifier
                    .height(300.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = circleLabel,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = formatAmount(amountsTotal),
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(backgroundColor = MaterialTheme.colors.background) {
            Column(modifier = Modifier.padding(12.dp)) {
                items.forEach { item ->
                    rows(item)
                }
            }
        }
    }
}
