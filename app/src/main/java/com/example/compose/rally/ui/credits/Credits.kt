package com.example.compose.rally.ui.credits

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
import com.example.compose.rally.data.Credit
import com.example.compose.rally.data.UserData
import com.example.compose.rally.ui.components.DepositRow
import com.example.compose.rally.ui.components.StatementBody

@Composable
fun CreditsScreen(
    bills: List<Credit> =  UserData.credits,
    onDepositClick: (String) -> Unit = {},

    ) {
    StatementBody(
        modifier = Modifier.clearAndSetSemantics { contentDescription = "Credits" },
        items = UserData.credits,
        amounts = { credit -> credit.amount },
        colors = { credit -> credit.color },
        amountsTotal = bills.map { credit -> credit.amount }.sum(),
        circleLabel = stringResource(R.string.due),
        rows = { credit ->

            DepositRow(
                modifier = Modifier.clickable {
                    onDepositClick(credit.name)
                },
                credit.name,
                credit.amount,
                credit.color
            )
        }
    )
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