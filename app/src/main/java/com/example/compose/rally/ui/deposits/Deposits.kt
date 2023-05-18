package com.example.compose.rally.ui.deposits

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.compose.rally.R
import com.example.compose.rally.data.Deposit
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.accounts.DropdownMenuContainer
import com.example.compose.rally.ui.components.ChangeAmount
import com.example.compose.rally.ui.components.DepositRow
import com.example.compose.rally.ui.components.StatementBody


enum class SortingOptionDeposit {
    NAME_ASCENDING, NAME_DESCENDING, PRICE_ASCENDING, PRICE_DESCENDING
}

@Composable
fun DepositsScreen(
    bills: List<Deposit> = UserData.deposits,
    onDepositClick: (String) -> Unit = {},
) {
    val amountsTotal = remember { bills.map { deposit -> deposit.amount }.sum() }

    var selectedSortingOption by remember { mutableStateOf(SortingOptionDeposit.NAME_ASCENDING) }
    val filteredAndSortedDeposits = remember(selectedSortingOption) {
        val sortedDeposits = when (selectedSortingOption) {
            SortingOptionDeposit.NAME_ASCENDING -> bills.sortedBy { it.name }
            SortingOptionDeposit.NAME_DESCENDING -> bills.sortedByDescending { it.name }
            SortingOptionDeposit.PRICE_ASCENDING -> bills.sortedBy { it.amount }
            SortingOptionDeposit.PRICE_DESCENDING -> bills.sortedByDescending { it.amount }
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
                        SortingOptionDeposit.NAME_ASCENDING -> "Name (A-Z)"
                        SortingOptionDeposit.NAME_DESCENDING -> "Name (Z-A)"
                        SortingOptionDeposit.PRICE_ASCENDING -> "Price (Low-High)"
                        SortingOptionDeposit.PRICE_DESCENDING -> "Price (High-Low)"
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
                    selectedSortingOption = SortingOptionDeposit.NAME_ASCENDING
                    expanded = false
                }) {
                    Text("Name (A-Z)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOptionDeposit.NAME_DESCENDING
                    expanded = false
                }) {
                    Text("Name (Z-A)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOptionDeposit.PRICE_ASCENDING
                    expanded = false
                }) {
                    Text("Price (Low-High)")
                }
                DropdownMenuItem(onClick = {
                    selectedSortingOption = SortingOptionDeposit.PRICE_DESCENDING
                    expanded = false
                }) {
                    Text("Price (High-Low)")
                }
            }
        }

        StatementBody(
            modifier = Modifier.fillMaxWidth(),
            items = filteredAndSortedDeposits,
            amountsTotal = amountsTotal,
            amounts = { deposit -> deposit.amount },
            colors = { deposit -> deposit.color },
            circleLabel = stringResource(R.string.due),
            rows = { deposit ->
                DepositRow(
                    modifier = Modifier.clickable {
                        onDepositClick(deposit.name)
                    },
                    name = deposit.name,
                    amount = deposit.amount,
                    color = deposit.color
                )
            }
        )
    }
}



@Composable
fun SingleDepositScreen(
    depositType: String? = UserData.deposits.first().name,
    navController: NavHostController
) {
    val deposit = remember(depositType) { UserData.getDeposit(depositType) }

    val deleteDeposit: () -> Unit = {
        UserData.deposits.remove(deposit)
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
            item = deposit,
            amountGetter = Deposit::amount,
            amountSetter = { item, amount ->
                item.amount = amount
            },
            onCloseDialog = closeDialog
        )
    }



    StatementBody(
        items = listOf(deposit),
        colors = { deposit.color },
        amounts = { deposit.amount },
        amountsTotal = deposit.amount,
        circleLabel = deposit.name,
    ) { row ->
        Column() {
            DepositRow(
                name = row.name,
                amount = row.amount,
                color = row.color
            )
            Button(onClick = openDialog,
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
                    .requiredHeight(65.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(text = "Edit")
            }

            Button(
                onClick = deleteDeposit,
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
                    .requiredHeight(65.dp),
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



