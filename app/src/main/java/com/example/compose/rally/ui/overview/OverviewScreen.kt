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

package com.example.compose.rally.ui.overview



import ExchangeRate
import TransactionWindow
import TransactionWindow2
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.compose.rally.R
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.components.AccountRow
import com.example.compose.rally.ui.components.BillRow
import com.example.compose.rally.ui.components.DepositRow
import com.example.compose.rally.ui.components.formatAmount
import java.util.Locale





@Composable
fun OverviewScreen(
    onClickSeeAllAccounts: () -> Unit = {},
    onClickSeeAllBills: () -> Unit = {},
    onClickSeeAllDeposits: () -> Unit = {},
    onClickSeeAllCredits: () -> Unit = {},
    onAccountClick: (String) -> Unit = {},
    onBillClick: (String) -> Unit = {},
    onDepositClick: (String) -> Unit = {},
    onCreditClick: (String) -> Unit = {}

) {

    var exchangeRates by remember { mutableStateOf(emptyList<ExchangeRate>()) }

    LaunchedEffect(Unit) {
        try {
            val response = ApiService.ApiServiceBuilder.apiService.getExchangeRates()
            exchangeRates = response // Update the exchange rates state variable
        } catch (e: Exception) {
            // Error handling
        }
    }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Overview Screen" }
    ) {



        ExchangeRatesCard(exchangeRates = exchangeRates)
        Spacer(Modifier.height(RallyDefaultPadding))

        TransactionWindow()
        Spacer(Modifier.height(RallyDefaultPadding))
        AccountsCard(
            onClickSeeAll = onClickSeeAllAccounts,
            onAccountClick = onAccountClick
        )
        Spacer(Modifier.height(RallyDefaultPadding))
        BillsCard(
            onClickSeeAll = onClickSeeAllBills,
            onBillClick = onBillClick

        )
        Spacer(Modifier.height(RallyDefaultPadding))

        TransactionWindow2()
        Spacer(Modifier.height(RallyDefaultPadding))
        DepositsCard(onClickSeeAll = onClickSeeAllDeposits, onDepositClick = onDepositClick)

        Spacer(Modifier.height(RallyDefaultPadding))

        CreditsCard(onClickSeeAll = onClickSeeAllCredits, onCreditClick = onCreditClick)
    }
}

/**
 * The Alerts card within the Rally Overview screen.
 */




@Composable
fun ExchangeRatesCard(exchangeRates: List<ExchangeRate>) {
    OverviewScreenCard2(
        title = "Exchange Rates",
        data = exchangeRates
    ) { exchangeRate ->
        Column(
            Modifier
                .clickable { /* Обробка кліку на елемент */ }
                .padding(12.dp)
        ) {
            Text(
                text = "Currency: ${exchangeRate.ccy}",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Base Currency: ${exchangeRate.base_ccy}",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp) //
            ) {
                Column(
                    Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = "Buy: ${exchangeRate.buy}",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Column(
                    Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Sale: ${exchangeRate.sale}",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}




@Composable
private fun <T> OverviewScreenCard2(
    title: String,
    data: List<T>,
    row: @Composable (T) -> Unit
) {
    Card {
        Column {
            Column(Modifier.padding(RallyDefaultPadding)) {
                Text(text = title, style = MaterialTheme.typography.subtitle2)
            }
            Column(Modifier.padding(start = 16.dp, top = 4.dp, end = 8.dp)) {
                data.forEach { item: T ->
                    row(item)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}



@Composable
private fun <T> OverviewScreenCard(
    title: String,
    amount: Float,
    onClickSeeAll: () -> Unit,
    values: (T) -> Float,
    colors: (T) -> Color,
    data: List<T>,
    row: @Composable (T) -> Unit
) {
    Card {
        Column {
            Column(Modifier.padding(RallyDefaultPadding)) {
                Text(text = title, style = MaterialTheme.typography.subtitle2)
                val amountText = "UAH " + formatAmount(
                    amount
                )
                Text(text = amountText, style = MaterialTheme.typography.h2)
            }
            OverViewDivider(data, values, colors)
            Column(Modifier.padding(start = 16.dp, top = 4.dp, end = 8.dp)) {
                data.take(SHOWN_ITEMS).forEach { row(it) }
                SeeAllButton(
                    modifier = Modifier.clearAndSetSemantics {
                        contentDescription = "All $title"
                    },
                    onClick = onClickSeeAll,
                )
            }
        }
    }
}

@Composable
private fun <T> OverViewDivider(
    data: List<T>,
    values: (T) -> Float,
    colors: (T) -> Color
) {
    Row(Modifier.fillMaxWidth()) {
        data.forEach { item: T ->
            Spacer(
                modifier = Modifier
                    .weight(values(item))
                    .height(1.dp)
                    .background(colors(item))
            )
        }
    }
}

/**
 * The Accounts card within the Rally Overview screen.
 */
@Composable
private fun CreditsCard(onClickSeeAll: () -> Unit, onCreditClick: (String) -> Unit) {

    val amount = UserData.credits.map { credit -> credit.amount }.sum()
    OverviewScreenCard(
        title = stringResource(R.string.credits),
        amount = amount,
        onClickSeeAll = onClickSeeAll,
        data = UserData.credits,
        colors = { Color(it.color) },
        values = { it.amount }
    ) { credit ->
        DepositRow(
            modifier = Modifier.clickable { onCreditClick(credit.name) },
            name = credit.name,
            amount = credit.amount,
            color = Color(credit.color)
        )
    }
}
@Composable
private fun DepositsCard(onClickSeeAll: () -> Unit, onDepositClick: (String) -> Unit) {

    val amount = UserData.deposits.map { deposit -> deposit.amount }.sum()
    OverviewScreenCard(
        title = stringResource(R.string.deposits),
        amount = amount,
        onClickSeeAll = onClickSeeAll,
        data = UserData.deposits,
        colors = { it.color },
        values = { it.amount }
    ) { deposit ->
        DepositRow(
            modifier = Modifier.clickable { onDepositClick(deposit.name) },
            name = deposit.name,
            amount = deposit.amount,
            color = deposit.color
        )
    }
}
@Composable
private fun AccountsCard(onClickSeeAll: () -> Unit, onAccountClick: (String) -> Unit) {

    val amount = UserData.accounts.map { account -> account.balance }.sum()
    OverviewScreenCard(
        title = stringResource(R.string.accounts),
        amount = amount,
        onClickSeeAll = onClickSeeAll,
        data = UserData.accounts,
        colors = { it.color },
        values = { it.balance }
    ) { account ->
        AccountRow(
            modifier = Modifier.clickable { onAccountClick(account.name) },
            name = account.name,
            amount = account.balance,
            color = account.color
        )
    }
}

/**
 * The Bills card within the Rally Overview screen.
 */
@Composable
private fun BillsCard(onClickSeeAll: () -> Unit, onBillClick: (String) -> Unit) {
    val amount = UserData.bills.map { bill -> bill.amount }.sum()
    OverviewScreenCard(
        title = stringResource(R.string.bills),
        amount = amount,
        onClickSeeAll = onClickSeeAll,
        data = UserData.bills,
        colors = { it.color },
        values = { it.amount }
    ) { bill ->
        BillRow(
            modifier = Modifier.clickable { onBillClick(bill.name) },
            name = bill.name,
            amount = bill.amount,
            color = bill.color
        )
    }
}

@Composable
private fun SeeAllButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
    ) {
        Text(stringResource(R.string.see_all))
    }
}

private val RallyDefaultPadding = 12.dp

private const val SHOWN_ITEMS = 3
