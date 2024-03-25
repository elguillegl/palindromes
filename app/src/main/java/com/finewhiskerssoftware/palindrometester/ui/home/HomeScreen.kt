package com.finewhiskerssoftware.palindrometester.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.finewhiskerssoftware.palindrometester.R
import com.finewhiskerssoftware.palindrometester.ui.theme.PalindromeTesterTheme

sealed class Result {
    data object NotSet: Result()
    data class Set(val value: Boolean): Result()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    val localContext = LocalContext.current
    var stringToTest by remember {
        mutableStateOf("")
    }

    var result: Result by remember {
        mutableStateOf(Result.NotSet)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.app_name))
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ))
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .padding(horizontal = 8.dp)
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(text = stringResource(R.string.enter_your_word))

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = stringToTest,
                    maxLines = 1,
                    singleLine = true,
                    onValueChange = {
                        stringToTest = it

                        if (result !is Result.NotSet)
                            result = Result.NotSet
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    val trimmedText = stringToTest.trim()

                    if (trimmedText.isEmpty()) {
                        Toast
                            .makeText(
                                localContext,
                                localContext.getString(R.string.enter_a_valid_value),
                                Toast.LENGTH_SHORT
                            )
                            .show()
                        return@Button
                    }

                    result = Result.Set(checkIsPalindrome(trimmedText))
                }) {
                    Text(text = stringResource(R.string.test_string_action))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val validationResult = result

            if (validationResult is Result.Set) {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.outcome_word))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringToTest)
                        }

                        if (validationResult.value) {
                            withStyle(style = SpanStyle(color = Color.Green)) {
                                append(stringResource(R.string.outcome_is_a_palindrome))
                            }
                        } else {
                            withStyle(style = SpanStyle(color = Color.Red)) {
                                append(stringResource(R.string.outcome_is_not_a_palindrome))
                            }
                        }
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

fun checkIsPalindrome(string: String): Boolean = string.reversed() == string

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    PalindromeTesterTheme {
        HomeScreen()
    }
}