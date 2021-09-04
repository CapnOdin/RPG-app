package com.example.rpgapp

import java.time.LocalDateTime
import java.util.ArrayList
import java.util.concurrent.ThreadLocalRandom

class Dice(val sides: Int) {
	
	fun roll(times: Int = 1): DiceRoll {
		val rolls = ArrayList<Int>(times)
		for(i in 1..times) {
			//Log.d("Roll", "$i")
			rolls.add(ThreadLocalRandom.current().nextInt(1, sides + 1))
		}
		return DiceRoll(sides, rolls)
	}
	
	class MultiDiceRoll {
		val dices = HashMap<Int, Dice>()
		val diceSides = HashMap<Int, Int>()
		val diceRolls = ArrayList<DiceRoll>()
		
		val time: LocalDateTime = LocalDateTime.now()
		
		fun add(dice: Dice) {
			if(!diceSides.containsKey(dice.sides)) {
				dices[dice.sides] = dice
				diceSides[dice.sides] = 1
			} else if(diceSides[dice.sides] != null) {
				diceSides[dice.sides] = diceSides[dice.sides]!!.plus(1)
			}
		}
		
		fun roll() {
			for(diceSides in diceSides) {
				diceRolls.add(dices[diceSides.component1()]!!.roll(diceSides.component2()))
			}
		}
		
		fun toStringList(): HashMap<Int, String> {
			val strings = HashMap<Int, String>()
			for(rolls in diceRolls) {
				strings[rolls.sides] = rolls.toString()
			}
			return strings
		}
	}
	
	data class DiceRoll(val sides: Int = 0, val diceRolls: ArrayList<Int> = ArrayList<Int>()) {
		val sum = diceRolls.sum()
		val time: LocalDateTime = LocalDateTime.now()
		
		init {
			diceRolls.sort()
		}
		
		fun toStringList(): List<String> {
			if(diceRolls.size > 1) {
				return listOf("D$sides", "{${diceRolls.joinToString(separator = ",")}}", "∑=$sum")
			} else if(diceRolls.size == 1) {
				return listOf("D$sides", "${diceRolls[0]}")
			}
			return listOf("D$sides")
		}
		
		override fun toString(): String {
			if(diceRolls.size > 1) {
				return "D$sides: {${diceRolls.joinToString(separator = ",")}} ∑: $sum"
			} else if(diceRolls.size == 1) {
				return "D$sides: ${diceRolls[0]}"
			}
			return "D$sides"
		}
	}
}