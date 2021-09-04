package com.example.rpgapp.gui

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class GuiLoader(context: Context) {
	private val filename = "gui"
	val file = File(context.filesDir, filename)

	init {
		Log.d(TAG, "File Exists ${file.exists()}")
		file.delete()
		if(!file.exists()) {
			file.createNewFile()
			val lst = listOf(
				GuiElement(JSONObject(summaryElement("STR", "STR_MOD", "STR_MOD"))),
				GuiElement(JSONObject(summaryElement("DEX", "DEX_MOD", "DEX_MOD"))),
				GuiElement(JSONObject(summaryElement("CON", "CON_MOD", "CON_MOD"))),
				GuiElement(JSONObject(summaryElement("INT", "INT_MOD", "INT_MOD"))),
				GuiElement(JSONObject(summaryElement("WIS", "WIS_MOD", "WIS_MOD"))),
				GuiElement(JSONObject(summaryElement("CHA", "CHA_MOD", "CHA_MOD"))),
				
				GuiElement(JSONObject(titleCounterElement("Loaded", "-1", "1"))),
				GuiElement(JSONObject(titleCounterElement("Ammo", "0", "99999999"))),
				GuiElement(JSONObject(titleCounterElement("Gunpowder", "0", "99999999"))),
				GuiElement(JSONObject(titleCounterElement("Misc_1", "-99999999", "99999999"))),
				GuiElement(JSONObject(titleCounterElement("Misc_2", "-99999999", "99999999")))
			)
			save(lst)
		}
	}

	private fun summaryElement(str: String, name: String, exp: String): String {
		return "{\"type\":\"summary\",\"children\":[${statElement(str)},${summaryTextElement(name, exp)}],\"expressions\":[]}"
	}

	private fun statElement(str: String): String {
		return "{\"type\":\"expRow\",\"children\":[],\"expressions\":[{\"name\":\"$str\",\"exp\":\"\",\"value\":0.0,\"integer\":true,\"isVar\":true},{\"name\":\"${str}_MOD\",\"exp\":\"(${str} + ${str}_BONUS - 10) / 2\",\"value\":0.0,\"integer\":true,\"isVar\":true},{\"name\":\"${str}_BONUS\",\"exp\":\"\",\"value\":0.0,\"integer\":true,\"isVar\":true}]}"
	}

	private fun summaryTextElement(name: String, exp: String): String {
		return "{\"type\":\"textSummary\",\"children\":[],\"expressions\":[{\"name\":$name,\"exp\":\"$exp\",\"value\":0.0,\"integer\":true,\"isVar\":false}]}"
	}
	
	private fun titleCounterElement(name: String, min: String, max: String): String {
		return "{\"type\":\"titleCounter\",\"children\":[],\"expressions\":[{\"name\":$name,\"exp\":\"0\",\"value\":0.0,\"integer\":true,\"isVar\":true}, {\"name\":\"min\",\"exp\":\"$min\",\"value\":0.0,\"integer\":true,\"isVar\":false}, {\"name\":\"max\",\"exp\":\"$max\",\"value\":0.0,\"integer\":true,\"isVar\":false}]}"
	}

	fun save(guiElements: List<GuiElement>) {
		file.writeText(guiElements.joinToString(separator = ",\n"))
	}

	fun load(): List<GuiElement> {
		val guiElements = ArrayList<GuiElement>()

		Log.d(TAG, "Loading GUI")
		val jLst = JSONArray("[${file.readText()}]")
		Log.d(TAG, "GUI Data: $jLst")

		for(i in 0 until jLst.length()) {
			guiElements.add(GuiElement(jLst[i] as JSONObject))
		}
		return guiElements
	}
	
	companion object {
		private const val TAG = "GuiLoader"
	}
}