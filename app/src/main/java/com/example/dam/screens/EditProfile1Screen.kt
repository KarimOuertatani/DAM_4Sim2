package com.example.dam.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.*

// Colors
private val BackgroundColor = Color(0xFF0B0B0B)
private val SecondaryTextColor = Color(0xFFBDBDBD)
private val PrimaryTextColor = Color.White
private val AccentGreen = Color(0xFF36C36A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile1Screen(navController: NavHostController, showDropdown: Boolean) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    // DatePicker state
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp, vertical = 22.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // --- Glassy back button + Title ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = CircleShape
                        )
                        .border(
                            width = 1.5.dp,
                            color = Color.White.copy(alpha = 0.30f),
                            shape = CircleShape
                        )
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            ambientColor = Color.Black.copy(alpha = 0.1f),
                            spotColor = Color.Black.copy(alpha = 0.15f)
                        )
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Edit Profile",
                    color = PrimaryTextColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- Form Fields ---
            Label("First Name")
            RoundedInputField(firstName, { firstName = it })

            Spacer(modifier = Modifier.height(12.dp))

            Label("Last Name")
            RoundedInputField(lastName, { lastName = it })

            Spacer(modifier = Modifier.height(12.dp))

            Label("Birthdate")
            // DatePicker Field
            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text(text = "Select date", color = SecondaryTextColor) },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date",
                            tint = AccentGreen
                        )
                    }
                },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clickable { showDatePicker = true },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = AccentGreen,
                    unfocusedBorderColor = SecondaryTextColor,
                    cursorColor = AccentGreen,
                    disabledTextColor = Color.White,
                    disabledBorderColor = SecondaryTextColor
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Label("Age")
            RoundedInputField(
                age,
                { age = it },
                hint = "Age"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Label("Gender")
            Spacer(modifier = Modifier.height(6.dp))

            val genders = listOf("Man", "Woman", "Non-binary")
            genders.forEach { g ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { gender = g }
                ) {
                    RadioButton(
                        selected = gender == g,
                        onClick = { gender = g },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AccentGreen,
                            unselectedColor = SecondaryTextColor
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = g, color = PrimaryTextColor, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- Next Button (Arrow) ---
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            ambientColor = Color.Black.copy(alpha = 0.1f),
                            spotColor = Color.Black.copy(alpha = 0.15f)
                        )
                        .background(
                            color = AccentGreen,
                            shape = CircleShape
                        )
                        .clickable { navController.navigate("editProfile2") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        // DatePicker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                            birthDate = formatter.format(Date(millis))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK", color = AccentGreen)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel", color = SecondaryTextColor)
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = Color(0xFF1A1A1A)
                )
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color(0xFF1A1A1A),
                        titleContentColor = Color.White,
                        headlineContentColor = Color.White,
                        weekdayContentColor = SecondaryTextColor,
                        subheadContentColor = Color.White,
                        yearContentColor = Color.White,
                        currentYearContentColor = AccentGreen,
                        selectedYearContainerColor = AccentGreen,
                        selectedDayContainerColor = AccentGreen,
                        todayContentColor = AccentGreen,
                        todayDateBorderColor = AccentGreen,
                        dayContentColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(text, color = SecondaryTextColor, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
}

@Composable
private fun RoundedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = hint, color = SecondaryTextColor) },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = AccentGreen,
            unfocusedBorderColor = SecondaryTextColor,
            cursorColor = AccentGreen
        )
    )
}