package com.bignerdranch.android.geomain

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel() {
    var currentIndex = 0
    var isCheater = false

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    var currentQuestionAnswered: Boolean
        get() {
            val answered = questionBank[currentIndex].answered
            return answered
        }
        set(value) { questionBank[currentIndex].answered = value }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToBack(){
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }
    val diffBetweenCurQB: Boolean
        get() = currentIndex < questionBank.size
    val questionBankSize: Int
        get() = questionBank.size
    val equalCurQB: Boolean
        get() = currentIndex == questionBank.size - 1



}