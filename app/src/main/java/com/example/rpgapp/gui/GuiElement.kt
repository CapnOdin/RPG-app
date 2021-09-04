package com.example.rpgapp.gui

import com.example.rpgapp.ExpressionState
import org.json.JSONArray
import org.json.JSONObject

@Suppress("UNCHECKED_CAST")
operator fun <T> JSONArray.iterator(): Iterator<T> = (0 until length()).asSequence().map { get(it) as T }.iterator()

class GuiElement {

	var type: String = ""
	var expressions: ArrayList<ExpressionState> = ArrayList()
	var children: ArrayList<GuiElement> = ArrayList()
	
	var str1: String = ""
	var str2: String = ""
	var str3: String = ""
	var str4: String = ""
	var num1: Double = 0.0
	var num2: Double = 0.0
	var num3: Double = 0.0
	var num4: Double = 0.0

	constructor(type: String, expressions: ArrayList<ExpressionState>) {
		this.type = type
		this.expressions = expressions
	}

	constructor(serialised: JSONObject) {
		type = serialised["type"] as String
		str1 = if(serialised.has("str1")) serialised["str1"] as String else ""
		str2 = if(serialised.has("str2")) serialised["str2"] as String else ""
		str3 = if(serialised.has("str3")) serialised["str3"] as String else ""
		str4 = if(serialised.has("str4")) serialised["str4"] as String else ""
		num1 = if(serialised.has("num1")) serialised["num1"] as Double else 0.0
		num2 = if(serialised.has("num2")) serialised["num2"] as Double else 0.0
		num3 = if(serialised.has("num3")) serialised["num3"] as Double else 0.0
		num4 = if(serialised.has("num4")) serialised["num4"] as Double else 0.0
		val jLstExpressions = serialised["expressions"] as JSONArray
		for(i in 0 until jLstExpressions.length()) {
			expressions.add(ExpressionState(jLstExpressions[i] as JSONObject))
		}
		val jLstGuiElements = serialised["children"] as JSONArray
		for(i in 0 until jLstGuiElements.length()) {
			children.add(GuiElement(jLstGuiElements[i] as JSONObject))
		}
	}

	override fun toString(): String {
		return "{\"type\":\"$type\",\"children\":[${children.joinToString(",")}],\"expressions\":[${expressions.joinToString(",")}]}"
	}

	fun getExpressions(): List<ExpressionState> {
		val expLst = ArrayList<ExpressionState>()
		expLst.addAll(expressions)
		for(child in children) {
			expLst.addAll(child.getExpressions())
		}
		return expLst
	}
}

