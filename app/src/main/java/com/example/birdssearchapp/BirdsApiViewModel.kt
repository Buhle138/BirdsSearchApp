package com.example.birdssearchapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class BirdsApiViewModel: ViewModel() {

    val state = mutableStateOf(MyScreen())

    public fun fetchBirds(){
        viewModelScope.launch {
            try {
                val birdsResponse = birdsResponse.searchRegion("ZA-EC")
                state.value = state.value.copy(listOfBirds = birdsResponse)
                Log.i("ListOfBirds", birdsResponse.toString())
            }catch (e: Exception){
                state.value = state.value.copy(
                    error = "Error fetching regions ${e.message}"
                )
                Log.i("errorRegionMsg", e.message.toString())
            }
        }
    }


}

data class MyScreen(
    var listOfBirds: List<Root2> = mutableListOf(),
    var error: String = ""
)