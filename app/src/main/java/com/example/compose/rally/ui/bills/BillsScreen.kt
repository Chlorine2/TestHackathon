package com.example.compose.rally.ui.bills

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
import com.example.compose.rally.data.Bill
import com.example.compose.rally.data.UserData
import com.example.compose.rally.data.UserData.bills
import com.example.compose.rally.ui.accounts.DropdownMenuContainer
import com.example.compose.rally.ui.components.BillRow
import com.example.compose.rally.ui.components.StatementBody
import com.example.compose.rally.ui.accounts.DropdownMenuContainer


/**
 * The Bills screen.
 */



enum class SortingOptionBill {
    NAME_ASCENDING, NAME_DESCENDING, AMOUNT_ASCENDING, AMOUNT_DESCENDING
}

@Composable
fun BillsScreen(
    bills: List<Bill> = UserData.bills,
    onBillClick: (String) -> Unit = {},
) {
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

        StatementBody(
            modifier = Modifier.fillMaxWidth(),
            items = filteredAndSortedBills,
            amountsTotal = bills.map { bill -> bill.amount }.sum(),
            circleLabel = stringResource(R.string.due),
            amounts = { bill -> bill.amount },
            colors = { bill -> bill.color },
            rows = { bill ->
                BillRow(
                    modifier = Modifier.clickable {
                        onBillClick(bill.name)
                    },
                    bill.name,
                    bill.amount,
                    bill.color
                )
            }
        )
    }
}




@Composable
fun SingleBillScreen(
    billType: String? = UserData.bills.first().name,
    navController: NavHostController
) {
    val bill = remember(billType) { UserData.getBill(billType) }

    val deleteBill: () -> Unit = {
        bills.remove(bill)
        navController.popBackStack()
    }

    StatementBody(
        items = listOf(bill),
        colors = { bill.color },
        amounts = { bill.amount },
        amountsTotal = bill.amount,
        circleLabel = bill.name,
    ) { row ->
        Column() {
            BillRow(
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
                onClick = deleteBill,
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