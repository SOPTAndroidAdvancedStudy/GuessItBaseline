package com.example.android.guesstheword.screens.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current word
    var word = ""

    // The current score
    // Nullable, Start with Null Value
    var score = MutableLiveData<Int>()

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    /**
     * Resets the list of words and randomizes the order
     */
    //단어를 리셋시키는 것은 단어 데이터에 대한 의사결정이므로 ViewModel에 있어야 함
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            //gameFinished()
        } else {
            word = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        // LiveData는 Nullable
        score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        // LiveData는 Nullable
        score.value = (score.value)?.plus(1)
        nextWord()
    }

    //생성 시 작동하는 함수
    init {
        resetList()
        nextWord()
        //LiveData 초기화, null로 시작하니까
        score.value = 0
    }
}