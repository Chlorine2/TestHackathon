package com.example.compose.rally.ui.credits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.compose.rally.R
import com.example.compose.rally.data.Credit
import com.example.compose.rally.data.UserData
import com.example.compose.rally.data.UserData.credits
import com.example.compose.rally.ui.accounts.DropdownMenuContainer
import com.example.compose.rally.ui.components.DepositRow
import com.example.compose.rally.ui.components.StatementBody
import com.example.compose.rally.ui.deposits.SortingOptionDeposit
import com.example.compose.rally.ui.accounts.DropdownMenuContainer
import com.example.compose.rally.ui.credits.CreditsScreen




enum class SortingOptionCredit {
    NAME_ASCENDING, NAME_DESCENDING, PRICE_ASCENDING, PRICE_DESCENDING
}

@Composable
fun CreditsScreen(
    bills: List<Credit> = UserData.credits,
    onDepositClick: (String) -> Unit = {},

    ) {


    val amountsTotal = remember { bills.map { deposit -> deposit.amount }.sum() }

    var selectedSortingOption by remember { mutableStateOf(SortingOptionCredit.NAME_ASCENDING) }
    val filteredAndSortedCredit = remember(selectedSortingOption) {
        val sortedDeposits = when (selectedSortingOption) {
            SortingOptionCredit.NAME_ASCENDING -> bills.sortedBy { it.name }
            SortingOptionCredit.NAME_DESCENDING -> bills.sortedByDescending { it.name }
            SortingOptionCredit.PRICE_ASCENDING -> bills.sortedBy { it.amount }
            SortingOptionCredit.PRICE_DESCENDING -> bills.sortedByDescending { it.amount }
        }
        sortedDeposits
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
                        SortingOptionCredit.NAME_ASCENDING -> "Name (A-Z)"
                        SortingOptionCredit.NAME_DESCENDING -> "Name (Z-A)"
                        SortingOptionCredit.PRICE_ASCENDING -> "Price (Low-High)"
                        SortingOptionCredit.PRICE_DESCENDING -> "Price (High-Low)"
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
                    selectedSortingOption = SortingOptionCredit.NAME_ASCENDING
                    expanded = false
                }) {
                    Text("Name (A-Z)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOptionCredit.NAME_DESCENDING
                    expanded = false
                }) {
                    Text("Name (Z-A)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOptionCredit.PRICE_ASCENDING
                    expanded = false
                }) {
                    Text("Price (Low-High)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOptionCredit.PRICE_DESCENDING
                    expanded = false
                }) {
                    Text("Price (High-Low)")
                }
            }
        }



        StatementBody(
            modifier = Modifier.fillMaxWidth(),
            items = filteredAndSortedCredit,
            amountsTotal = amountsTotal,
            amounts = { credit -> credit.amount },
            colors = { credit -> credit.color },
            circleLabel = stringResource(R.string.due),
            rows = { credit ->
                DepositRow(
                    modifier = Modifier.clickable {
                        onDepositClick(credit.name)
                    },
                    name = credit.name,
                    amount = credit.amount,
                    color = credit.color
                )
            }
        )
    }
}


@Composable
fun SingleCreditScreen(
    creditType: String? = UserData.credits.first().name,
    navController: NavHostController
) {
    val credit = remember(creditType) { UserData.getCredit(creditType) }

    val deleteCredit: () -> Unit = {
        UserData.credits.remove(credit)
        navController.popBackStack()
    }

    StatementBody(
        items = listOf(credit),
        colors = { credit.color },
        amounts = { credit.amount },
        amountsTotal = credit.amount,
        circleLabel = credit.name,
    ) { row ->
        Column() {
            DepositRow(
                name = row.name,
                amount = row.amount,
                color = row.color
            )
            Button(onClick = {}, shape = RoundedCornerShape(100.dp),
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
                onClick = deleteCredit,
                shape = RoundedCornerShape(100.dp),
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