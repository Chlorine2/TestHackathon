package com.example.compose.rally.ui.deposits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.compose.rally.R
import com.example.compose.rally.data.Deposit
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.components.BillRow
import com.example.compose.rally.ui.components.DepositRow
import com.example.compose.rally.ui.components.StatementBody

@Composable
fun DepositsScreen(
    bills: List<Deposit> =  UserData.deposits,
    onDepositClick: (String) -> Unit = {},

    ) {
    StatementBody(
        modifier = Modifier.clearAndSetSemantics { contentDescription = "Deposits" },
        items = UserData.deposits,
        amounts = { deposit -> deposit.amount },
        colors = { deposit -> deposit.color },
        amountsTotal = bills.map { deposit -> deposit.amount }.sum(),
        circleLabel = stringResource(R.string.due),
        rows = { deposit ->

            DepositRow(
                modifier = Modifier.clickable {
                    onDepositClick(deposit.name)
                },
                deposit.name,
                deposit.amount,
                deposit.color
            )
        }
    )
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
                onClick = deleteDeposit,
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