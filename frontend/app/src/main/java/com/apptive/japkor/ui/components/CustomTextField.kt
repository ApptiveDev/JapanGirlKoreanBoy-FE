package com.apptive.japkor.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apptive.japkor.ui.theme.CustomColor

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isNumberOnly: Boolean = false   // ⬅ 숫자만 입력 여부 추가
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (isNumberOnly) {
                // 숫자만 허용
                val filtered = newValue.filter { it.isDigit() }
                onValueChange(filtered)
            } else {
                onValueChange(newValue)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = CustomColor.gray200,
            focusedBorderColor = CustomColor.gray300
        ),
        placeholder = {
            CustomText(
                text = placeholder,
                type = CustomTextType.body,
                color = CustomColor.gray300
            )
        },
        textStyle = androidx.compose.ui.text.TextStyle(
            textAlign = TextAlign.Start
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions =
            when {
                isPassword -> KeyboardOptions(keyboardType = KeyboardType.Password)
                isNumberOnly -> KeyboardOptions(keyboardType = KeyboardType.Number)
                else -> KeyboardOptions.Default
            }
    )
}
