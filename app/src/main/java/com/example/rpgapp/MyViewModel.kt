package com.example.rpgapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.rpgapp.gui.Gui
import java.util.ArrayList

class MyViewModel: ViewModel() {
	var gui: Gui = Gui()
	
	val diceRolls = ArrayList<Dice.MultiDiceRoll>()
	
	val diceRollsCurrent = mutableStateOf(List(0) { Dice.MultiDiceRoll() })
	
	var combinedDiceRoll = Dice.MultiDiceRoll()
	
	var diceMenuState = mutableStateOf(listOf(""))
	var diceMenuMulti = mutableStateOf(false)
}