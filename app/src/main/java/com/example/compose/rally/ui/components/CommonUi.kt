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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.example.compose.rally.R
import java.text.DecimalFormat

/**
 * A row representing the basic information of an Account.
 */
@Composable
fun DepositRow(
    modifier: Modifier = Modifier,
    name: String,
    amount: Float,
    color: Color
) {
    BaseRow(
        modifier = modifier,
        color = color,
        title = name,
        amount = amount,
        negative = false
    )
}
@Composable
fun AccountRow(
    modifier: Modifier = Modifier,
    name: String,
    number: Int,
    amount: Float,
    color: Color
) {
    BaseRow(
        modifier = modifier,
        color = color,
        title = name,
        subtitle = stringResource(R.string.account_redacted) + AccountDecimalFormat.format(number),
        amount = amount,
        negative = false
    )
}

/**
 * A row representing the basic information of a Bill.
 */
@Composable
fun BillRow(
    modifier: Modifier = Modifier,
    name: String,
    due: String,
    amount: Float,
    color: Color
) {
    BaseRow(
        modifier = modifier,
        color = color,
        title = name,
        subtitle = "Due $due",
        amount = amount,
        negative = true
    )
}

@Composable
private fun BaseRow(
    modifier: Modifier = Modifier,
    color: Color,
    title: String,
    subtitle: String,
    amount: Float,
    negative: Boolean
) {
    val Sign = if (negative) "–UAH " else "UAH "
    val formattedAmount = formatAmount(amount)
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), elevation = 5.dp, shape = RoundedCornerShape(100.dp)) {


        Row(
            modifier = modifier
                .height(68.dp)
                .clearAndSetSemantics {
                    contentDescription =
                        "$title account ending in ${subtitle}, current balance $Sign$formattedAmount"
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            val typography = MaterialTheme.typography
            AccountIndicator(
                color = color,
                modifier = Modifier
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier) {
                Text(text = title, style = typography.body1)

            }
            Spacer(Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = Sign,
                    style = typography.h6,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = formattedAmount,
                    style = typography.h6,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp)
                )
            }
        }
    }
}

/**
 * A vertical colored line that is used in a [BaseRow] to differentiate accounts.
 */
@Composable
private fun AccountIndicator(color: Color, modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .size(20.dp, 100.dp)
            .background(color = color)
    )
}



fun formatAmount(amount: Float): String {
    return AmountDecimalFormat.format(amount)
}

private val AccountDecimalFormat = DecimalFormat("####")
private val AmountDecimalFormat = DecimalFormat("#,###.##")

/**
 * Used with accounts and bills to create the animated circle.
 */
fun <E> List<E>.extractProportions(selector: (E) -> Float): List<Float> {
    val total = this.sumOf { selector(it).toDouble() }
    return this.map { (selector(it) / total).toFloat() }
}
