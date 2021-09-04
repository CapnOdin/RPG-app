package com.example.rpgapp

import androidx.compose.runtime.mutableStateOf
import org.json.JSONObject
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.mXparser
import org.mariuszgromada.math.mxparser.parsertokens.Token
import java.util.*
import kotlin.collections.HashMap

class ExpressionState {
	
	var name = ""
	var exp = ""
	var value = 0.0
	private var integer = true
	var isVar = true
	val id = UUID.randomUUID().toString()
	
	var valueText = mutableStateOf("$value")
	var expression = Expression(exp)
	var usedBy = arrayListOf<String>()
	var dependsOn = arrayListOf<String>()
	
	constructor(name: String, exp: String = "", value: Double = 0.0, integer: Boolean = true, isVar: Boolean = true) {
		this.name = name
		this.exp = exp
		this.value = value
		this.integer = integer
		this.isVar = isVar
		valueText = mutableStateOf("$value")
		expression = Expression(exp)
	}
	
	constructor(serialised: JSONObject) {
		this.name = serialised["name"] as String
		this.exp = serialised["exp"] as String
		this.value = serialised["value"] as Double
		this.integer = serialised["integer"] as Boolean
		this.isVar = serialised["isVar"] as Boolean
		valueText = mutableStateOf("$value")
		expression = Expression(exp)
	}
	
	fun setExpression(expStr: String, expressionStates: HashMap<String, ExpressionState>) {
		exp = expStr
		expression.expressionString = expStr
		expression.removeAllConstants()
		findDependencies()
		notifyDependencies(expressionStates)
		recalculateExpression(expressionStates)
	}
	
	fun recalculateExpression(expressionStates: HashMap<String, ExpressionState>) {
		expression.removeAllConstants()
		expression.expressionString = exp
		applyVariables(expressionStates)
		val result = expression.calculate()
		if(value != result) {
			value = result
			updateDependentExpressions(expressionStates)
		}
		valueText.value = if(integer) {
			"${value.toInt()}"
		} else {
			"$value"
		}
	}
	
	fun calculateExternalExpression(expStr: String, expressionStates: HashMap<String, ExpressionState>) {
		expression.removeAllConstants()
		expression.expressionString = expStr
		applyVariables(expressionStates)
		val result = expression.calculate()
		if(value != result) {
			value = result
			updateDependentExpressions(expressionStates)
		}
	}
	
	private fun applyVariables(expressionStates: HashMap<String, ExpressionState>) {
		val tokensList = expression.copyOfInitialTokens
		for(t in tokensList) {
			if(t.tokenTypeId == Token.NOT_MATCHED) {
				if(t.tokenStr.toString() in expressionStates) {
					if(expressionStates[t.tokenStr.toString()]!!.integer) {
						expression.defineConstant(t.tokenStr.toString(), expressionStates[t.tokenStr.toString()]!!.value.toInt().toDouble())
					} else {
						expression.defineConstant(t.tokenStr.toString(), expressionStates[t.tokenStr.toString()]!!.value)
					}
				} else {
					mXparser.consolePrintln("token = " + t.tokenStr.toString() + ", hint = " + t.looksLike)
				}
			}
		}
	}
	
	private fun notifyDependencies(expressionStates: HashMap<String, ExpressionState>) {
		for(e in dependsOn) {
			if(e in expressionStates) {
				if(!expressionStates[e]!!.usedBy.contains(if(isVar) name else id)) {
					expressionStates[e]!!.usedBy.add(if(isVar) name else id)
				}
			} else {
				mXparser.consolePrintln("Undeclared Variable = $e")
			}
		}
	}
	
	private fun findDependencies() {
		val tokensList = expression.copyOfInitialTokens
		for(t in tokensList) {
			if(t.tokenTypeId == Token.NOT_MATCHED) {
				if(!dependsOn.contains(t.tokenStr.toString())) {
					dependsOn.add(t.tokenStr.toString())
				}
			}
		}
	}
	
	private fun updateDependentExpressions(expressionStates: HashMap<String, ExpressionState>) {
		for(e in usedBy) {
			expressionStates[e]!!.setExpression(expressionStates[e]!!.exp, expressionStates)
		}
	}
	
	override fun toString(): String {
		return "{\"name\":\"$name\",\"exp\":\"$exp\",\"value\":$value,\"integer\":$integer,\"isVar\":$isVar}"
	}
}