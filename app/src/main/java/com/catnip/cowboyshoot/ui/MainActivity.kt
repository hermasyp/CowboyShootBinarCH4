package com.catnip.cowboyshoot.ui

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.catnip.cowboyshoot.R
import com.catnip.cowboyshoot.databinding.ActivityMainBinding
import com.catnip.cowboyshoot.enum.CharacterMovementPosition
import com.catnip.cowboyshoot.enum.CharacterPosition
import com.catnip.cowboyshoot.enum.CharacterShootState
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.java.simpleName

    private var posUser: Int = CharacterMovementPosition.MIDLLE.value
    private var posComp: Int = CharacterMovementPosition.MIDLLE.value
    private var isGameFinished: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setInitialState()
        setClickEvent()
    }

    private fun setInitialState() {
        //set pos user for idle
        setCharacterMovement(posUser, CharacterPosition.LEFT, CharacterShootState.IDLE)
        //set pos for computer
        setCharacterMovement(posComp, CharacterPosition.RIGHT, CharacterShootState.IDLE)
        //set text for inital start button
        binding.tvStatusGame.text = getString(R.string.text_start_game)
    }

    private fun setClickEvent() {
        binding.ivArrowUp.setOnClickListener {
            if (!isGameFinished && posUser > CharacterMovementPosition.TOP.value) {
                posUser--
                Log.d(TAG, "setClickEvent: $posUser")
                setCharacterMovement(posUser, CharacterPosition.LEFT, CharacterShootState.IDLE)
            }
        }
        binding.ivArrowDown.setOnClickListener {
            if (!isGameFinished && posUser <  CharacterMovementPosition.BOTTOM.value) {
                posUser++
                Log.d(TAG, "setClickEvent: $posUser")
                setCharacterMovement(posUser, CharacterPosition.LEFT, CharacterShootState.IDLE)
            }
        }
        binding.tvStatusGame.setOnClickListener {
            if (isGameFinished) {
                resetGame()
            } else {
                startGame()
            }

        }
    }

    private fun resetGame() {
        Log.d(TAG, "resetGame: before pos user = {$posUser} pos comp = {$posUser} ")
        isGameFinished = false
        posUser = CharacterMovementPosition.MIDLLE.value
        posComp = CharacterMovementPosition.MIDLLE.value
        binding.tvWinnerGame.text = ""
        setInitialState()
        Log.d(TAG, "resetGame: after pos user = {$posUser} pos comp = {$posUser} ")
    }


    private fun startGame() {
        //set random position for computer
        posComp = Random.nextInt(0, 3)
        //set shoot state user
        setCharacterMovement(posUser, CharacterPosition.LEFT, CharacterShootState.SHOOT)
        //set shoot state for enemy
        setCharacterMovement(posComp, CharacterPosition.RIGHT, CharacterShootState.SHOOT)
        //logic for winners
        if (posUser == posComp) {
            //user lose
            setCharacterMovement(posUser, CharacterPosition.LEFT, CharacterShootState.DEAD)
            binding.tvWinnerGame.text = getString(R.string.text_user_lose)
        } else {
            //user win
            setCharacterMovement(posComp, CharacterPosition.RIGHT, CharacterShootState.DEAD)
            binding.tvWinnerGame.text = getString(R.string.text_user_win)
        }
        isGameFinished = true
        binding.tvStatusGame.text = getString(R.string.text_reset_game)

    }

    private fun setCharacterMovement(
        position: Int,
        characterPosition: CharacterPosition,
        characterShootState: CharacterShootState
    ) {
        val ivCharTop: ImageView?
        val ivCharMiddle: ImageView?
        val ivCharBottom: ImageView?
        val imgResChar: Drawable?
        if (characterPosition == CharacterPosition.LEFT) {
            ivCharTop = binding.ivPlayerLeft0
            ivCharMiddle = binding.ivPlayerLeft1
            ivCharBottom = binding.ivPlayerLeft2
            imgResChar = ContextCompat.getDrawable(
                this,
                when (characterShootState) {
                    CharacterShootState.IDLE -> R.drawable.ic_cowboy_left_shoot_false
                    CharacterShootState.SHOOT -> R.drawable.ic_cowboy_left_shoot_true
                    CharacterShootState.DEAD -> R.drawable.ic_cowboy_left_dead
                }
            )
        } else {
            ivCharTop = binding.ivPlayerRight0
            ivCharMiddle = binding.ivPlayerRight1
            ivCharBottom = binding.ivPlayerRight2
            imgResChar = ContextCompat.getDrawable(
                this,
                when (characterShootState) {
                    CharacterShootState.IDLE -> R.drawable.ic_cowboy_right_shoot_false
                    CharacterShootState.SHOOT -> R.drawable.ic_cowboy_right_shoot_true
                    CharacterShootState.DEAD -> R.drawable.ic_cowboy_right_dead
                }
            )
        }

        when (CharacterMovementPosition.fromInt(position)) {
            CharacterMovementPosition.TOP -> {
                ivCharTop.visibility = View.VISIBLE
                ivCharMiddle.visibility = View.INVISIBLE
                ivCharBottom.visibility = View.INVISIBLE
                ivCharTop.setImageDrawable(imgResChar)
            }
            CharacterMovementPosition.MIDLLE -> {
                ivCharTop.visibility = View.INVISIBLE
                ivCharMiddle.visibility = View.VISIBLE
                ivCharBottom.visibility = View.INVISIBLE
                ivCharMiddle.setImageDrawable(imgResChar)
            }
            CharacterMovementPosition.BOTTOM -> {
                ivCharTop.visibility = View.INVISIBLE
                ivCharMiddle.visibility = View.INVISIBLE
                ivCharBottom.visibility = View.VISIBLE
                ivCharBottom.setImageDrawable(imgResChar)
            }
        }
    }


}