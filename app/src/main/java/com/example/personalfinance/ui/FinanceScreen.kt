package com.example.personalfinance.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.personalfinance.ui.theme.MonetaryNegative
import com.example.personalfinance.ui.theme.MonetaryPositive
import kotlin.math.abs

private data class Transaction(
    val id: Int,
    val description: String,
    val amount: Double,
    val category: String,
    val type: TransactionType
)

enum class TransactionType { Income, Expense }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen() {
    val transactions = remember {
        mutableStateListOf(
            Transaction(1, "Зарплата", 120000.0, "Работа", TransactionType.Income),
            Transaction(2, "Аренда", -38000.0, "Жилье", TransactionType.Expense),
            Transaction(3, "Продукты", -12000.0, "Повседневные расходы", TransactionType.Expense)
        )
    }

    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.Expense) }

    val income = transactions.filter { it.type == TransactionType.Income }.sumOf { it.amount }
    val expenses = transactions.filter { it.type == TransactionType.Expense }.sumOf { abs(it.amount) }
    val balance = income - expenses

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Личные финансы",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        SummaryRow(balance = balance, income = income, expenses = expenses)

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Новая операция", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Категория") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Сумма") },
                    keyboardOptions = KeyboardOptions.Default,
                    modifier = Modifier.fillMaxWidth()
                )

                SegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = type == TransactionType.Expense,
                        onClick = { type = TransactionType.Expense },
                        shape = SegmentedButtonDefaults.itemShape(0, 2)
                    ) {
                        Text(text = "Расход")
                    }
                    SegmentedButton(
                        selected = type == TransactionType.Income,
                        onClick = { type = TransactionType.Income },
                        shape = SegmentedButtonDefaults.itemShape(1, 2)
                    ) {
                        Text(text = "Доход")
                    }
                }

                Button(
                    onClick = {
                        val parsedAmount = amount.replace(',', '.').toDoubleOrNull()
                        if (parsedAmount != null && description.isNotBlank() && category.isNotBlank()) {
                            val signedAmount = if (type == TransactionType.Expense) -abs(parsedAmount) else abs(parsedAmount)
                            val nextId = (transactions.maxOfOrNull { it.id } ?: 0) + 1
                            transactions.add(
                                Transaction(
                                    id = nextId,
                                    description = description.trim(),
                                    amount = signedAmount,
                                    category = category.trim(),
                                    type = type
                                )
                            )
                            description = ""
                            amount = ""
                            category = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Добавить операцию")
                }
            }
        }

        Text(text = "История", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(transactions) { transaction ->
                TransactionItem(transaction = transaction)
            }
        }
    }
}

@Composable
private fun SummaryRow(balance: Double, income: Double, expenses: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(title = "Баланс", amount = balance)
        SummaryCard(title = "Доход", amount = income, accentPositive = true)
        SummaryCard(title = "Расход", amount = -expenses, accentPositive = false)
    }
}

@Composable
private fun SummaryCard(title: String, amount: Double, accentPositive: Boolean = true) {
    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Text(
                text = "%.2f ₽".format(amount),
                style = MaterialTheme.typography.titleMedium,
                color = if (amount >= 0) MonetaryPositive else MonetaryNegative,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = transaction.description, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Text(
                text = if (transaction.type == TransactionType.Income) {
                    "+%.2f ₽".format(transaction.amount)
                } else {
                    "-%.2f ₽".format(abs(transaction.amount))
                },
                color = if (transaction.type == TransactionType.Income) MonetaryPositive else MonetaryNegative,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
