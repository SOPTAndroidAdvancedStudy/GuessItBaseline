/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    //액티비티의 viewModel을 사용하여 Fragment에서 viewModel을 사용한다
    private val viewModel: GameViewModel by activityViewModels()

    //단어리스트와 점수는 ViewModel로 옮겨 Configuration이 변경되어도 상태가 유지되게 한다

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        //DataBinding은 View에 종속됨 -> 움직이지 않는다
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
        Log.d("ViewModel", "ViewModel Get from activity")

        //DataBinding: xml의 data와 viewModel 연결
        binding.gameViewModel = viewModel
        //layout에 직접 LiveData 연결
        binding.lifecycleOwner = this

//        resetList()
//        nextWord()
        //ViewModel이 만들어졌을 때 resetList가 실행 -> Not Fragment Createed

        /*
        게임 종료될 때 gameFinish 호출
        게임이 끝났으면 isFinish의 상태를 false로 바꾼다
        새로 게임을 시작하면 그 게임은 끝난게 아니니까
        */
        viewModel.isFinish.observe(viewLifecycleOwner, Observer { isFinished ->
            if (isFinished) {
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        return binding.root

    }


    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(
                viewModel.score.value ?: 0
        )
        findNavController(this).navigate(action)
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer?.vibrate(pattern, -1)
            }
        }
    }
}
