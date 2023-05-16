import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.compose.rally.data.Account
import com.example.compose.rally.data.Bill
import com.example.compose.rally.data.Credit
import com.example.compose.rally.data.Deposit
import com.example.compose.rally.data.UserData
import com.example.compose.rally.data.UserData.accounts
import com.example.compose.rally.data.UserData.bills

@Composable
fun TransactionCard1(
    onShowIncomeWindowChanged: (Boolean) -> Unit,
    onShowBillsWindowChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.background(color = Color(0xFFf2f2f2)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Transaction",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TransactionButton(text = "Income") {
                    // Open new window for entering data
                    onShowIncomeWindowChanged(true)
                }
                TransactionButton(text = "Bills") {
                    onShowBillsWindowChanged(true)
                }
            }
        }
    }
}

@Composable
fun TransactionCard2(
    onShowCreditWindowChanged: (Boolean) -> Unit,
    onShowDepositWindowChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.background(color = Color(0xFFf2f2f2)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Transaction",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TransactionButton(text = "Deposits"){
                    onShowDepositWindowChanged(true)
                }
                TransactionButton(text = "Credits"){
                    onShowCreditWindowChanged(true)
                }

            }
        }
    }
}

@Composable
fun TransactionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun IncomeWindow(
    onSubmit: (Account) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    val color by remember { mutableStateOf(Color.White) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = balance,
            onValueChange = { balance = it },
            label = { Text("Balance") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val existingAccount = accounts.find { it.name == name }
                    if (existingAccount != null) {
                        val updatedBalance = existingAccount.balance + (balance.toFloatOrNull() ?: 0f)
                        existingAccount.balance = updatedBalance
                    } else {
                        val account = Account(
                            name = name,
                            balance = balance.toFloatOrNull() ?: 0f,
                            color = color,
                        )
                        onSubmit(account)
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Submit")
            }



            Button(
                onClick = onCancel,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}


@Composable
fun BillsWindow(
    onSubmit: (Bill) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var due by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val color by remember { mutableStateOf(Color.White) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = due,
            onValueChange = { due = it },
            label = { Text("Due") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )
        // TODO: Add a color picker for the user to select a color

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val existingBill = bills.find { it.name == name }
                    if (existingBill != null) {
                        val updatedAmount = existingBill.amount + (amount.toFloatOrNull() ?: 0f)
                        existingBill.amount = updatedAmount
                    } else {
                        val bill = Bill(
                            name = name,
                            due = due,
                            amount = amount.toFloatOrNull() ?: 0f,
                            color = color
                        )
                        onSubmit(bill)
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Submit")
            }

            Button(
                onClick = onCancel,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}


@Composable
fun DepositWindow(
    onSubmit: (Deposit) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val color by remember { mutableStateOf(Color.White) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val existingDeposit = UserData.deposits.find { it.name == name }
                    if (existingDeposit != null) {
                        val updatedAmount = existingDeposit.amount + (amount.toFloatOrNull() ?: 0f)
                        existingDeposit.amount = updatedAmount
                    } else {
                        val deposit = Deposit(
                            name = name,
                            amount = amount.toFloatOrNull() ?: 0f,
                            color = color
                        )
                        onSubmit(deposit)
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Submit")
            }

            Button(
                onClick = onCancel,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
fun CreditWindow(
    onSubmit: (Credit) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val color by remember { mutableStateOf(Color.White) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val existingCredit = UserData.credits.find { it.name == name }
                    if (existingCredit != null) {
                        val updatedAmount = existingCredit.amount + (amount.toFloatOrNull() ?: 0f)
                        existingCredit.amount = updatedAmount
                    } else {
                        val credit = Credit(
                            name = name,
                            amount = amount.toFloatOrNull() ?: 0f,
                            color = color
                        )
                        onSubmit(credit)
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Submit")
            }

            Button(
                onClick = onCancel,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}



@SuppressLint("MutableCollectionMutableState")
@Composable
fun TransactionWindow() {
    var showIncomeWindow by remember { mutableStateOf(false) }
    var showBillsWindow by remember { mutableStateOf(false) }
    val accounts by remember { mutableStateOf(UserData.accounts) }
    val bills by remember { mutableStateOf(UserData.bills) }

    if (showIncomeWindow) {
        IncomeWindow(
            onSubmit = { account ->
                accounts.add(account)
                showIncomeWindow = false
            },
            onCancel = { showIncomeWindow = false }
        )
    } else if (showBillsWindow) {
        BillsWindow(
            onSubmit = { bill ->
                val account = accounts.find { it.name == bill.name }
                account?.let {
                    val updatedBalance = it.balance + bill.amount
                    it.balance = updatedBalance
                }
                bills.add(bill)
                showBillsWindow = false
            },
            onCancel = { showBillsWindow = false }
        )
    } else {
        TransactionCard1(
            onShowIncomeWindowChanged = { show ->
                showIncomeWindow = show
            },
            onShowBillsWindowChanged = { show ->
                showBillsWindow = show
            }
        )
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TransactionWindow2() {
    var showCreditWindow by remember { mutableStateOf(false) }
    val credits by remember { mutableStateOf(UserData.credits) }
    var showDepositWindow by remember { mutableStateOf(false) }
    val deposits by remember { mutableStateOf(UserData.deposits) }

    if (showDepositWindow) {
        DepositWindow(
            onSubmit = { deposit ->
                deposits.add(deposit)
                showDepositWindow = false
            },
            onCancel = { showDepositWindow = false }
        )
    } else if (showCreditWindow) {
        CreditWindow(
            onSubmit = { credit ->
                credits.add(credit)
                showCreditWindow = false
            },
            onCancel = { showCreditWindow = false }
        )
    } else {
        TransactionCard2(
            onShowDepositWindowChanged = { show ->
                showDepositWindow = show
            },
            onShowCreditWindowChanged = { show ->
                showCreditWindow = show
            }
        )
    }
}
