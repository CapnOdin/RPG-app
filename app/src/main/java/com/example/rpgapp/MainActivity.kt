package com.example.rpgapp

import android.content.res.Configuration.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpgapp.ui.theme.MyComposeTheme
import org.json.JSONObject
import java.util.*

//var file: File = null

class MainActivity : ComponentActivity() {

	//var character = JSONObject("")

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		/*
		val dice: Dice = Dice(20)
		val res = dice.roll(100000)
		for(i in 1..20) {
			var n = res.diceRolls.count { it == i }
			Log.d(TAG, "$i: $n  ${n - 5000}")
		}
		*/
		//file = File(filesDir, "character.json")

		//character = JSONObject(file.readText())

		setContent {
			HomeScreen()
		}
	}

	override fun onPause() {
		super.onPause()
		//file.writeText(character.toString(4))
	}
}

val TAG = "compose fun"

//private val a = R.drawable.ic_d20
//private val c = mutableStateOf(a)
private val c = mutableStateOf(R.drawable.ic_d20)
private val m = mutableStateOf(
	Modifier
		.height(50.dp)
		.width(50.dp))

//val diceRolls = ArrayList<Dice.DiceRoll>()

val diceRolls = ArrayList<Dice.MultiDiceRoll>()

val diceRollsCurrent = mutableStateOf(List<Dice.MultiDiceRoll>(0) { Dice.MultiDiceRoll() })

var combinedDiceRoll = Dice.MultiDiceRoll()

var ammo = mutableStateOf(0)
var loaded = mutableStateOf(0)
var gunpowder = mutableStateOf(0)
var misc1 = mutableStateOf(0)
var misc2 = mutableStateOf(0)

//var infoVisible = mutableStateOf(false)
//var infoContent = mutableStateOf((() -> {}))

@Composable
fun HomeScreen(/*...*/) {
	val materialBlue700 = Color(0xFF1976D2)
	var clicks by remember{mutableStateOf(0)}
	Box() {
		Scaffold(
			drawerContent = { MyDrawer() },
			topBar = {
				TopAppBar(title = { Text("TopAppBar") }, backgroundColor = materialBlue700)
			},
			floatingActionButtonPosition = FabPosition.End,
			floatingActionButton = { DiceMenuButtonTest(buttonSize = 50.dp) },
			content = {
				LazyColumn(
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier.fillMaxWidth()
				) {
					/*
					items(5) {
						NewsStory()
						ClickCounter(clicks, onClick = { clicks++ })
					}
					*/
					items(1) {
						TitleCounter("Loaded", loaded, min = -1, max = 1)
					}
					items(1) {
						TitleCounter("Ammo", ammo, min = 0)
					}
					items(1) {
						TitleCounter("Gunpowder", gunpowder, min = 0)
					}
					items(1) {
						TitleCounter("Misc 1", misc1)
					}
					items(1) {
						TitleCounter("Misc 2", misc2)
					}
				}
			}
		)
		//InfoBox()
	}
}

@Composable
fun TitleCounter(text: String, extcount: MutableState<Int>, modifier: Modifier = Modifier, min: Int = -999999, max: Int = 999999) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier.padding(10.dp)
	) {
		Text(text = text)
		AdjustableCounter(extcount, min = min, max = max, modifier = modifier)
	}
}

/*
@Composable
fun InfoBox() {
	var visible by infoVisible
	var content by infoContent
	val onClick = {
		visible = false
		
	}
	if(visible) {
		Box(
			modifier = Modifier.fillMaxSize().background(Color(0x50777777)).clickable { onClick() }
		) {
			Box(
				modifier = Modifier.clickable { onClick() }
			) {
				content()
			}
		}
	}
}
*/

@Composable
fun CharacterPage(character: JSONObject) {

}

fun getDiceDrawable(dice: String): Int {
	return when(dice) {
		"d4" -> R.drawable.ic_d4_abs
		"d6" -> R.drawable.ic_d6_abs
		"d8" -> R.drawable.ic_d8_abs
		"d10" -> R.drawable.ic_d10_abs
		"d12" -> R.drawable.ic_d12_abs
		"d20" -> R.drawable.ic_d20_abs
		else -> R.drawable.ic_launcher_foreground
	}
}

fun getDiceDrawable(dice: Int): Int {
	return when(dice) {
		4 -> R.drawable.ic_d4_abs
		6 -> R.drawable.ic_d6_abs
		8 -> R.drawable.ic_d8_abs
		10 -> R.drawable.ic_d10_abs
		12 -> R.drawable.ic_d12_abs
		20 -> R.drawable.ic_d20_abs
		else -> R.drawable.ic_porcupine
	}
}

var dicemenuState = mutableStateOf(listOf(""))
var dicemenuMulti = mutableStateOf(false)

@Composable
fun DiceMenuButtonTest(buttonSize: Dp = 50.dp, buttonSpacing: Dp = 6.dp) {
	var list by dicemenuState
	var multi by dicemenuMulti

	Box(
		contentAlignment = Alignment.BottomEnd
	) {
		Box(
			contentAlignment = Alignment.BottomCenter,
			modifier = Modifier.padding(bottom = buttonSize / 2)
		) {
			LazyColumn(
				horizontalAlignment = Alignment.End,
				verticalArrangement = Arrangement.spacedBy(buttonSpacing),
				contentPadding = PaddingValues(top = buttonSize, bottom = buttonSize / 2 + buttonSpacing),
				modifier = Modifier.animateContentSize()
			) {
				itemsIndexed(list) { index, item ->
					when (item) {
						"multi" -> {
							MyFloatButton() {
								ImageButton(
									R.drawable.ic_rolling_dices,
									modifier = Modifier.requiredSize(buttonSize),
									onClick = {
										if(multi) {
											combinedDiceRoll.roll()
											diceRolls.add(0, combinedDiceRoll)
											diceRollsCurrent.value = diceRolls.toList()
											combinedDiceRoll = Dice.MultiDiceRoll()
											list = listOf("+")
											multi = false
										} else {
											multi = true
										}
									}
								)
							}
						}
						else -> {
							if(item.matches(Regex("d\\d+"))) {
								//MyFloatButton() { MultiDiceButton(item.substring(1).toInt(), multi, onClick = {}, modifier = Modifier.requiredSize(buttonSize)) }
								FloatMultiDiceButton(item.substring(1).toInt(), multi, onClick = {}, modifier = Modifier.requiredSize(buttonSize))
							}
						}
					}
				}
			}
		}

		if(list.size < 2) {
			MyFloatButton() {
				ImageButton(
					R.drawable.ic_cubes,
					modifier = Modifier.requiredSize(buttonSize),
					onClick = {
						list = listOf("d4", "d6", "d8", "d10", "d12", "d20", "multi", "")
						multi = false
						Log.d(TAG, list.toString());
					}
				)
			}
		} else {
			MyFloatButton() {
				ImageButton(
					R.drawable.ic_plain_arrow,
					modifier = Modifier.requiredSize(buttonSize),
					onClick = {
						list = listOf("")
						multi = false
						Log.d(TAG, list.toString());
					}
				)
			}
		}
	}
}

@Composable
fun DiceMenuButton() {
	var list by remember {mutableStateOf(listOf("+"))}
	var multi by remember {mutableStateOf(false)}
	FloatingActionButton(
		onClick = {
			list = if(list.size > 1) {
				listOf("+")
			} else {
				listOf("d4", "d6", "d8", "d10", "d12", "d20", "multi", "-")
			}
		}
	) {
		LazyColumn(horizontalAlignment = Alignment.End) {
			itemsIndexed(list) { index, item ->
				when (item) {
					"d4" -> {
						MultiDiceButton(4, multi, onClick = {})
					}
					"d6" -> {
						MultiDiceButton(6, multi, onClick = {})
					}
					"d8" -> {
						MultiDiceButton(8, multi, onClick = {})
					}
					"d10" -> {
						MultiDiceButton(10, multi, onClick = {})
					}
					"d12" -> {
						MultiDiceButton(12, multi, onClick = {})
					}
					"d20" -> {
						MultiDiceButton(20, multi, onClick = {})
					}
					"multi" -> {
						ImageButton(
							R.drawable.ic_rolling_dices,
							modifier = Modifier.requiredSize(50.dp),
							onClick = {
								if(multi) {
									combinedDiceRoll.roll()
									diceRolls.add(0, combinedDiceRoll)
									diceRollsCurrent.value = diceRolls.toList()
									combinedDiceRoll = Dice.MultiDiceRoll()
									list = listOf("+")
									multi = false
								} else {
									multi = true
								}
							}
						)
					}
					"+" -> {
						ImageButton(
							R.drawable.ic_cubes,
							modifier = Modifier.requiredSize(50.dp),
							onClick = {
								list =
									listOf("d4", "d6", "d8", "d10", "d12", "d20", "multi", "-")
								multi = false
							}
						)
					}
					"-" -> {
						ImageButton(
							R.drawable.ic_plain_arrow,
							modifier = Modifier.requiredSize(50.dp),
							onClick = {
								list = listOf("+")
								multi = false
							}
						)
					}
					else -> {
					
					}
				}
			}
		}
	}
}

@Composable
fun MyFloatButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {},
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
	backgroundColor: Color = MaterialTheme.colors.secondary,
	contentColor: Color = contentColorFor(backgroundColor),
	elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
	content: @Composable () -> Unit
) {
	FloatingActionButton(
		onClick = onClick,
		modifier = modifier,
		interactionSource = interactionSource,
		shape = shape,
		backgroundColor = backgroundColor,
		contentColor = contentColor,
		elevation = elevation,
		content = content
	)
}

@Composable
fun FloatMultiDiceButton(sides: Int, multi: Boolean, onClick: (sides: Int) -> Unit, modifier: Modifier = Modifier) {
	var clicks by remember{mutableStateOf(0)}
	if(multi) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.defaultMinSize(minWidth = 30.dp, minHeight = 10.dp)
					.background(color = Color.LightGray, shape = RoundedCornerShape(5.dp))
			) {
				Text("$clicks", modifier = Modifier.align(Alignment.Center), fontSize = 18.sp)
			}
			Spacer(modifier = Modifier.requiredSize(width = 10.dp, height = 0.dp))
			MyFloatButton(
				onClick = {
					clicks++
					combinedDiceRoll.add(Dice(sides))
				}
			) {
				DiceImage(getDiceDrawable(sides),modifier = modifier)
			}
		}
	} else {
		MyFloatButton() {
			DiceButton(sides, onClick = {}, modifier = modifier)
		}
	}
}


@Composable
fun ClickCounter(clicks: Int, onClick: () -> Unit) {
	Button(onClick = onClick) {
		Text("I've been clicked $clicks times")
	}
}

@Composable
fun MultiDiceButton(sides: Int, multi: Boolean, onClick: (sides: Int) -> Unit, modifier: Modifier = Modifier) {
	var clicks by remember{mutableStateOf(0)}
	if(multi) {
		Row() {
			Text("$clicks:", modifier = Modifier.align(Alignment.CenterVertically), fontSize = 15.sp)
			ImageButton(
				getDiceDrawable(sides),
				onClick = {
					clicks++
					combinedDiceRoll.add(Dice(sides))
				},
				modifier = modifier
			)
		}
	} else {
		DiceButton(sides, onClick = {}, modifier = modifier)
	}
}

@Composable
fun DiceButton(sides: Int, onClick: (sides: Int) -> Unit, modifier: Modifier = Modifier) {
	val context = LocalContext.current
	var rotate by remember {mutableStateOf(false)}
	val rotationAngle by animateFloatAsState(
		targetValue = if (rotate) 360F else 0F
	)
	val dice = Dice(sides)
	ImageButton(
		getDiceDrawable(dice.sides),
		onClick = {
			rotate = !rotate
			val diceRoll = Dice.MultiDiceRoll()
			diceRoll.add(dice)
			diceRoll.roll()
			diceRolls.add(0, diceRoll)
			diceRollsCurrent.value = diceRolls.toList()
			Toast.makeText(context, diceRoll.diceRolls[0].toString(), Toast.LENGTH_SHORT).show()
		},
		modifier = modifier
			.wrapContentSize()
			.rotate(rotationAngle)
	)
}

@Composable
fun ImageButton(drawable: Int, onClick: () -> Unit, modifier: Modifier = Modifier.requiredSize(50.dp)) {
	IconButton(
		onClick = {
			onClick()
		},
		modifier = modifier.wrapContentSize()
	) {
		DiceImage(drawable, modifier = modifier)
	}
}

@Composable
fun DiceImage(dice: Int, modifier: Modifier = Modifier) {
	Image(
		painter = painterResource(dice),
		contentDescription = null,
		modifier = modifier,
		contentScale = ContentScale.Crop
	)
}

@Composable
fun MyDrawer() {
	if(LocalConfiguration.current.orientation == ORIENTATION_PORTRAIT) {
		Column(
			modifier = Modifier,
			verticalArrangement = Arrangement.SpaceAround
		) {
			MyMenu()
			DiceRollHistory()
		}
	} else {
		Row(
			modifier = Modifier,
			horizontalArrangement = Arrangement.SpaceAround
		) {
			MyMenu(modifier = Modifier.weight(1.0f, true))
			DiceRollHistory(modifier = Modifier.weight(1.0f, true))
		}
	}
}

@Composable
fun MyMenu(modifier: Modifier = Modifier) {
	Column(modifier = modifier) {
		MenuButton(R.drawable.ic_person,        "Character",        onClick = {})
		MenuButton(R.drawable.ic_crossed_swords,"Combat",           onClick = {})
		MenuButton(R.drawable.ic_skills,        "Skills & Feats",   onClick = {})
		MenuButton(R.drawable.ic_spell_book,    "Magic",            onClick = {})
		MenuButton(R.drawable.ic_backpack,      "Inventory",        onClick = {})
	}
}

@Composable
fun MenuButton(icon: Int, title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
	Button(
		onClick = onClick,
		modifier = modifier
			.padding(5.dp)
			.clip(shape = RoundedCornerShape(4.dp))
	) {
		Row(
			modifier = Modifier.fillMaxWidth()
		) {
			Image(
				painter = painterResource(icon),
				contentDescription = null,
				modifier = Modifier
					.size(25.dp)
					.align(Alignment.CenterVertically),
				contentScale = ContentScale.Crop
			)
			Text(
				text = title,
				fontSize = 25.sp,
				modifier = Modifier.align(Alignment.CenterVertically)
			)
		}
	}
}

@Composable
fun DiceRollHistory(modifier: Modifier = Modifier) {
	val list by diceRollsCurrent
	Card(
		modifier = modifier
			.fillMaxHeight()
			.fillMaxWidth()
			.padding(2.dp),
		backgroundColor = Color.LightGray,
		border = BorderStroke(2.dp, Color.LightGray)
	) {
		LazyColumn() {
			itemsIndexed(list) { index, item ->
				RollHistoryElement(item)
			}
		}
	}
}

/*
@Composable
fun DiceRollHistory(modifier: Modifier = Modifier) {
	val list by diceRollsCurrent
	Card(
		modifier = modifier
			.fillMaxHeight()
			.fillMaxWidth()
			.padding(2.dp),
		backgroundColor = Color.LightGray,
		border = BorderStroke(2.dp, Color.LightGray)
	) {
		LazyColumn() {
			itemsIndexed(list) { index, item ->
				Box(
					modifier = Modifier.padding(2.dp)
				) {
					Column(
						modifier = Modifier
							.wrapContentHeight()
							.fillMaxWidth()
							.padding(horizontal = 12.dp, vertical = 2.dp)
							.background(
								color = Color(0xFF93B4D5),
								shape = RoundedCornerShape(5.dp)
							)
					) {
						for (d in item.diceRolls) {
							Row() {
								DiceImage(
									getDiceDrawable(d.sides),
									Modifier
										.requiredSize(25.dp)
										.align(Alignment.CenterVertically)
								)
								Text(
									d.toString(),
									fontSize = 17.sp,
									modifier = Modifier.align(Alignment.CenterVertically)
								)
							}
						}
					}
				}
			}
		}
	}
}
*/

@Composable
fun RollHistoryElement(multiDiceRoll: Dice.MultiDiceRoll, modifier: Modifier = Modifier) {
	val segments = listOf(40, 100, 60, 20, 20)
	var size by remember {mutableStateOf(1f)}
	Box(
		modifier = modifier
			.padding(2.dp)
	) {
		Column(
			modifier = Modifier
				.wrapContentHeight()
				.fillMaxWidth()
				.padding(horizontal = 12.dp, vertical = 2.dp)
				.background(
					color = Color(0xFF93B4D5),
					shape = RoundedCornerShape(5.dp)
				)
				.clickable {
					size = if (size > 1) {
						1f
					} else {
						1.3f
					}
				}
		) {
			for (d in multiDiceRoll.diceRolls) {
				Row() {
					DiceImage(
						getDiceDrawable(d.sides),
						Modifier
							.requiredSize((25 * size).dp)
							.align(Alignment.CenterVertically)
					)
					AlignedTextRow(d.toStringList(), segments, size)
				}
			}
		}
	}
}

@Composable
fun AlignedTextRow(textSegments: List<String>, segments: List<Int>, size: Float) {
	val middle = textSegments.subList(1, if (textSegments.count() > 2) textSegments.count() - 1 else textSegments.count())
	Row(
		modifier = Modifier.animateContentSize()
	) {
		Text(
			text = textSegments[0],
			fontSize = (17 * size).sp,
			modifier = Modifier
				.width((segments[0] * size).dp)
				.align(Alignment.CenterVertically))
		Row(modifier = Modifier.weight(1f, true)) {
			for ((i, text) in middle.withIndex()) {
				val modifier = Modifier.align(Alignment.CenterVertically)
				Text(
					text = text,
					fontSize = (17 * size).sp,
					modifier = Modifier
						//.width(segments[i + 1].dp)
						.align(Alignment.CenterVertically)
				)
			}
		}
		if(textSegments.count() > 2) {
			Text(
				text = textSegments[textSegments.lastIndex],
				fontSize = (17 * size).sp,
				modifier = Modifier
					//.width(segments[textSegments.lastIndex].dp)
					.align(Alignment.CenterVertically)
			)
		}
	}
}

@Composable
fun AdjustableCounter(extcount: MutableState<Int>, modifier: Modifier = Modifier, min: Int = -999999, max: Int = 999999) {
	var count by extcount
	Row(
		modifier = modifier.background(
			color = Color(0xFF93B4D5),
			shape = RoundedCornerShape(5.dp)
		)
	) {
		ImageButton(
			drawable = R.drawable.ic_plain_arrow_down,
			onClick = {
				if(count > min) {
					count -= 1
				}
			},
			modifier = Modifier.align(Alignment.CenterVertically)
		)
		Text(
			text = count.toString(),
			fontSize = 17.sp,
			modifier = Modifier.align(Alignment.CenterVertically)
		)
		ImageButton(
			drawable = R.drawable.ic_plain_arrow_up,
			onClick = {
				if(count < max) {
					count += 1
				}
			},
			modifier = Modifier.align(Alignment.CenterVertically)
		)
	}
}





@Composable
fun Greeting(name: String) {
	Text(text = "Hello $name!")
}

@Composable
fun NewsStory() {
	MaterialTheme {
		Column(
			modifier = Modifier.padding(16.dp)
		) {
			Image(
				painter = painterResource(R.drawable.header),
				contentDescription = null,
				modifier = Modifier
					.height(180.dp)
					.fillMaxWidth()
					.clip(shape = RoundedCornerShape(4.dp)),
				contentScale = ContentScale.Crop
			)
			Spacer(Modifier.height(16.dp))
			
			Text(
				"A day wandering through the sandhills " +
						"in Shark Fin Cove, and a few of the " +
						"sights I saw",
				style = typography.h6,
				maxLines = 2,
				overflow = TextOverflow.Ellipsis)
			Text("Davenport, California",
				style = typography.body2)
			Text("December 2018",
				style = typography.body2)
			
		}
	}
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
	MyComposeTheme {
		// A surface container using the 'background' color from the theme
		Surface(color = MaterialTheme.colors.background) {
			Greeting("Android")
		}
	}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
	HomeScreen()
}