package com.catnip.cowboyshoot.enum

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
enum class CharacterMovementPosition(val value: Int) {
    TOP(0),
    MIDLLE(1),
    BOTTOM(2);
    companion object{
        fun fromInt(value: Int) = values().first(){it.value == value}
    }
}