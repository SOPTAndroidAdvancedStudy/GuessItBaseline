package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current word
    var word = ""

    // The current score
    // Nullable, Start with Null Value
    private val privateScore = MutableLiveData<Int>()
    // 외부에 공개하는 Property는 값이 변경되지 않는 것으로
    // 내부에서는 privateScore의 값을 활용하여 넘겨준다
    val score: LiveData<Int>
        get() = privateScore

    // 게임이 종료되었는 지 여부에 관한 Boolean value
    private val privateIsFinish = MutableLiveData<Boolean>()
    val isFinish : LiveData<Boolean>
        get() = privateIsFinish

//    private val privateTimeLong = MutableLiveData<Long>()
//    val timeLong : LiveData<Long>
//        get() = privateTimeLong

    private val privateTime = MutableLiveData<String>()
    val time : LiveData<String>
        get() = privateTime

    private var timeLimit : Long = 0

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    // Timer
    private val timer : CountDownTimer

    //생성 시 작동하는 함수
    init {
        resetList()
        nextWord()
        //LiveData 초기화, null로 시작하니까
        privateScore.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                timeLimit = millisUntilFinished / ONE_SECOND
                privateTime.value = DateUtils.formatElapsedTime(timeLimit)
            }

            override fun onFinish() {
                if(wordList.isNotEmpty()) {
                    privateIsFinish.value = true
                }
            }
        }
        timer.start()
    }

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
            privateIsFinish.value = true
            resetList()
        } else {
            word = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        // LiveData는 Nullable
        privateScore.value = (privateScore.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        // LiveData는 Nullable
        privateScore.value = (privateScore.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        privateIsFinish.value = false
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 5000L
    }

}