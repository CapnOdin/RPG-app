package com.example.rpgapp.gui

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.ExpressionState
import com.example.rpgapp.ImageButton
import com.example.rpgapp.MyViewModel
import com.example.rpgapp.R

const val TAG = "GuiElements"

@Composable
fun GuiElementComposable(vm: MyViewModel, guiElement: GuiElement, modifier: Modifier = Modifier) {
	Log.d("GuiElementComposable", "$guiElement")
	when(guiElement.type) {
		"stat" -> {
			CharacterStat(vm, guiElement.expressions[0], guiElement.expressions[1], guiElement.expressions[2], modifier = modifier)
		}
		"expRow" -> {
			ExpressionRow(vm, guiElement.expressions, modifier = modifier)
		}
		"expLazyRow" -> {
			LazyExpressionRow(vm, guiElement.expressions, modifier = modifier)
		}
		"statAlt" -> {
			CharacterStatAlternate(vm, guiElement.expressions[0], guiElement.expressions[1], guiElement.expressions[2], modifier = modifier)
		}
		"textSummary" -> {
			TextSummary(guiElement.expressions[0], modifier = modifier)
		}
		"summary" -> {
			SummaryComposable(vm, guiElement.children[0], guiElement.children[1], modifier = modifier)
		}
		"rowComposite" -> {
			RowComposite(vm, guiElement.children, modifier = modifier)
		}
		"titleCounter" -> {
			TitleCounter(vm, guiElement.expressions[0].name, guiElement.expressions[0], modifier = modifier, min = guiElement.expressions[1].exp.toInt(), max = guiElement.expressions[2].exp.toInt())
		}
		else -> {
			if(guiElement.children.size > 0) {
				Column {
					for(cg in guiElement.children) {
						GuiElementComposable(vm, cg)
					}
				}
			}
		}
	}
}

@Composable
fun RowComposite(vm: MyViewModel, guiElements: ArrayList<GuiElement>, modifier: Modifier) {
	val list by remember {mutableStateOf(guiElements.toList())}
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		for(guiElement in list) {
			GuiElementComposable(vm, guiElement)
		}
	}
}

@Composable
fun ExpressionRow(vm: MyViewModel, expressions: ArrayList<ExpressionState>, modifier: Modifier) {
	val list by remember {mutableStateOf(expressions.toList())}
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		for(exp in list) {
			DefaultExpressionField(vm, exp, modifier = Modifier.weight(1f, fill = false))
		}
	}
}

@Composable
fun LazyExpressionRow(vm: MyViewModel, expressions: ArrayList<ExpressionState>, modifier: Modifier) {
	val list by remember {mutableStateOf(expressions.toList())}
	LazyRow(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		itemsIndexed(list) { _, item ->
			DefaultExpressionField(vm, item)
		}
	}
}

@Composable
fun SummaryComposable(vm: MyViewModel, guiElement: GuiElement, summary: GuiElement, modifier: Modifier = Modifier) {
	Log.d("SummaryComposable", "Gui: $guiElement")
	Log.d("SummaryComposable", "Sum: $summary")
	var focused by remember { mutableStateOf(false) }
	Row(
		Modifier
			.clickable(onClick = { focused = true })
			.animateContentSize()
			.then(modifier),
		verticalAlignment = Alignment.CenterVertically
	) {
		if(focused) {
			GuiElementComposable(vm, guiElement, modifier = Modifier.weight(1f))
			Button(
				onClick = {focused = false}
			) {
				Image(
					painter = painterResource(R.drawable.ic_launcher_foreground),
					contentDescription = null,
					modifier = Modifier.size(30.dp),
					contentScale = ContentScale.Crop
				)
			}
		} else {
			GuiElementComposable(vm, summary)
		}
	}
}

@Composable
fun CharacterStat(vm: MyViewModel, ability: ExpressionState, modify: ExpressionState, bonus: ExpressionState, modifier: Modifier = Modifier) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		DefaultExpressionField(vm, ability, modifier = Modifier.weight(1f, fill = false))
		DefaultExpressionField(vm, modify, modifier = Modifier.weight(1f, fill = false))
		DefaultExpressionField(vm, bonus, modifier = Modifier.weight(1f, fill = false))
	}
}

@Composable
fun CharacterStatAlternate(vm: MyViewModel, ability: ExpressionState, modify: ExpressionState, bonus: ExpressionState, modifier: Modifier = Modifier) {
	var focused by remember { mutableStateOf(false) }
	Row(
		Modifier
			.clickable(onClick = { focused = true })
			.animateContentSize()
			.then(modifier),
		verticalAlignment = Alignment.CenterVertically
	) {
		if(focused) {
			DefaultExpressionField(vm, ability, modifier = Modifier.weight(1f, fill = false))
			DefaultExpressionField(vm, modify, modifier = Modifier.weight(1f, fill = false))
			DefaultExpressionField(vm, bonus, modifier = Modifier.weight(1f, fill = false))
			Button(onClick = {focused = false}) {
				Image(
					painter = painterResource(R.drawable.ic_launcher_foreground),
					contentDescription = null,
					modifier = Modifier.size(30.dp),
					contentScale = ContentScale.Crop
				)
			}
		} else {
			TextSummary(modify)
		}
	}
}

@Composable
fun TextSummary(summary: ExpressionState, modifier: Modifier = Modifier) {
	Log.d("TextSummary", "${summary.name}: ")
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text("${summary.name}: ", fontFamily = FontFamily.Monospace, fontSize = 18.sp)
		ShowStat(summary, fontFamily = FontFamily.Monospace, fontSize = 16.sp)
	}
}

@Composable
fun ShowStat(state: ExpressionState, modifier: Modifier = Modifier, fontFamily: FontFamily = FontFamily.Monospace, fontSize: TextUnit = 13.sp) {
	val valueText by state.valueText
	Text(text = valueText, fontFamily = fontFamily, fontSize = fontSize, modifier = modifier)
}

@Composable
fun TitleCounter(vm: MyViewModel, text: String, state: ExpressionState, modifier: Modifier = Modifier, min: Int = -999999, max: Int = 999999) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier.padding(10.dp)
	) {
		Text(text = text)
		AdjustableCounter(vm, state, min = min, max = max, modifier = modifier)
	}
}

@Composable
fun AdjustableCounter(vm: MyViewModel, state: ExpressionState, modifier: Modifier = Modifier, min: Int = -999999, max: Int = 999999) {
	val valueText by state.valueText
	Row(
		modifier = modifier.background(
			color = Color(0xFF93B4D5),
			shape = RoundedCornerShape(5.dp)
		)
	) {
		ImageButton(
			drawable = R.drawable.ic_plain_arrow_down,
			onClick = {
				if(valueText.toDouble() > min) {
					state.exp = (valueText.toDouble() - 1).toString()
					Log.d(TAG, state.exp)
					state.recalculateExpression(vm.gui.expressionStates)
				}
			},
			modifier = Modifier.align(Alignment.CenterVertically)
		)
		Text(
			text = valueText,
			fontSize = 17.sp,
			modifier = Modifier.align(Alignment.CenterVertically)
		)
		ImageButton(
			drawable = R.drawable.ic_plain_arrow_up,
			onClick = {
				if(valueText.toDouble() < max) {
					state.exp = (valueText.toDouble() + 1).toString()
					Log.d(TAG, state.exp)
					state.recalculateExpression(vm.gui.expressionStates)
				}
			},
			modifier = Modifier.align(Alignment.CenterVertically)
		)
	}
}

@Composable
fun DefaultExpressionField(vm: MyViewModel, state: ExpressionState, modifier: Modifier = Modifier) {
	ExpressionField(state, modifier,
		onValueChange = {
			state.exp = it
			state.calculateExternalExpression(it, vm.gui.expressionStates)
		},
		onEditStart = {state.valueText.value = state.exp},
		onEditEnd = {state.setExpression(it, vm.gui.expressionStates)}
	)
}

@Composable
fun ExpressionField(state: ExpressionState, modifier: Modifier = Modifier, onValueChange: (expression: String) -> Unit = {}, onEditStart: (text: String) -> Unit = {}, onEditEnd: (text: String) -> Unit = {}) {
	var focused by remember { mutableStateOf(false) }
	var valueText by state.valueText
	TextField(
		value = valueText,
		onValueChange = {
			valueText = it
			if(focused) {
				onValueChange(it)
			}
		},
		label = { Text(state.name) },
		keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
		singleLine = true,
		modifier = Modifier
			.padding(5.dp)
			.wrapContentWidth()
			.onFocusChanged {
				if (focused && !it.isFocused) {
					focused = false
					onEditEnd(valueText)
				} else if (!focused && (it.isFocused || it.hasFocus)) {
					focused = true
					onEditStart(valueText)
				}
			}
			.then(modifier)
	)
}