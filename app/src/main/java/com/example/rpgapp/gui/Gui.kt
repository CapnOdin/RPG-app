package com.example.rpgapp.gui

import android.content.Context
import android.util.Log
import com.example.rpgapp.ExpressionState

class Gui {
	private var loaded = false

	var expressionStates = HashMap<String, ExpressionState>()
	var guiElements = ArrayList<GuiElement>()

	fun load(context: Context) {
		if(!loaded) {
			val guiLoader = GuiLoader(context)
			guiElements.addAll(guiLoader.load())
			Log.d(TAG, "Expressions:")
			for(guiElement in guiElements) {
				for(exp in guiElement.getExpressions()) {
					Log.d(TAG, "$exp")
					expressionStates[if(exp.isVar) exp.name else exp.id] = exp
				}
			}
		}
		loaded = true
	}
	
	companion object {
		private const val TAG = "Gui"
	}
}