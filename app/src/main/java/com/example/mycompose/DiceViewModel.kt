package com.example.mycompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiceViewModel: ViewModel() {
	private val diceLiveData: MutableLiveData<Dice> = MutableLiveData<Dice>()
	
	fun getDice(): LiveData<Dice>? {
		return diceLiveData
	}
	
	fun doAction() {
		// depending on the action, do necessary business logic calls and update the
		// userLiveData.
	}
}