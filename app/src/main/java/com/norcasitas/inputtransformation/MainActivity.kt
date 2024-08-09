package com.norcasitas.inputtransformation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.InputTransformation
import androidx.compose.foundation.text2.input.TextFieldBuffer
import androidx.compose.foundation.text2.input.allCaps
import androidx.compose.foundation.text2.input.forEachChange
import androidx.compose.foundation.text2.input.then
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.norcasitas.inputtransformation.ui.theme.InputTransformationTheme

class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalFoundationApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      InputTransformationTheme {
        LazyColumn {
          item {
            Column {
              var text by remember { mutableStateOf("hello") }
              Text(text = "default sin filter")
              BasicTextField2(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color.Cyan.copy(alpha = .3f)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
              )
            }
            Spacer(Modifier.size(20.dp))
          }
          item {
            Column {
              var text by remember { mutableStateOf("hello") }
              Text(text = "filter all caps")
              BasicTextField2(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color.Cyan.copy(alpha = .3f)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                inputTransformation = InputTransformation.allCaps(Locale.current)
              )
              Spacer(Modifier.size(20.dp))
            }
          }
          item {
            Column {
              var text by remember { mutableStateOf("hello") }
              var textField: TextFieldBuffer? by remember { mutableStateOf(null) }
              var rangee: TextRange? by remember { mutableStateOf(null) }
              Text(text = "filter only letters")
              BasicTextField2(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color.Cyan.copy(alpha = .3f)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                inputTransformation = { _, valueWithChanges ->
                  // only update inserted content
                  valueWithChanges.changes.forEachChange { range, _ ->
                    textField = valueWithChanges
                    rangee = range
                    if (!range.collapsed) {
                      valueWithChanges.replace(
                        range.min,
                        range.max,
                        valueWithChanges.asCharSequence().substring(range).filter { it.isLetter() }
                      )
                    }
                  }
                }
              )
              Text(text = "valueWithChanges: $textField")
              Text(text = "Range: $rangee")
              Spacer(Modifier.size(20.dp))
            }
          }
          item {
            Column {
              var text by remember { mutableStateOf("") }
              Text(text = "filter only letters different approach")
              BasicTextField2(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color.Cyan.copy(alpha = .3f)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                inputTransformation = { _, valueWithChanges ->
                  if (valueWithChanges.asCharSequence().any { it.isLetter().not() }) {
                    valueWithChanges.revertAllChanges()
                  }
                }
              )
              Spacer(Modifier.size(20.dp))
            }
          }
          item {
            Column {
              var text by remember { mutableStateOf("hello") }
              Text(text = "current filter and mapper")
              BasicTextField2(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color.Cyan.copy(alpha = .3f)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                inputTransformation = InputTransformation { _, valueWithChanges ->
                  // only update inserted content
                  valueWithChanges.changes.forEachChange { range, _ ->
                    if (!range.collapsed) {
                      valueWithChanges.replace(
                        range.min,
                        range.max,
                        valueWithChanges.asCharSequence().substring(range).filter { it.isLetter() }
                      )
                    }
                  }
                }.then(InputTransformation.byValue { _, proposed -> proposed.toString() })
              )
              Spacer(Modifier.size(20.dp))
            }
          }
        }
      }
    }
  }
}
