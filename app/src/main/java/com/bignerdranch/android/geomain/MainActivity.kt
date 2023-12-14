package com.bignerdranch.android.geomain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private var correctAnswersCount = 0

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)


        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            disableAnswerButtons()
            quizViewModel.currentQuestionAnswered = true
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
            disableAnswerButtons()
            quizViewModel.currentQuestionAnswered = true
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            if(quizViewModel.equalCurQB){
                nextButton.isEnabled = false
                nextButton.visibility = View.INVISIBLE
            }
        }
        prevButton.setOnClickListener {
            quizViewModel.moveToBack()
            updateQuestion()
        }

        updateQuestion()
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
    private fun updateQuestion() {
        if (!quizViewModel.currentQuestionAnswered){
            enableAnswerButtons()
        }
        else
        {
            disableAnswerButtons()
        }
        if (quizViewModel.diffBetweenCurQB ) {
            val questionTextResId = quizViewModel.currentQuestionText
            questionTextView.setText(questionTextResId)
        } else {
            showFinalScore()
        }
    }
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (userAnswer == correctAnswer) {
            correctAnswersCount++
            getString(R.string.correct_toast)
        }
        else {
            getString(R.string.incorrect_toast)
        }
        Snackbar.make(findViewById(android.R.id.content), messageResId, Snackbar.LENGTH_SHORT).show()

        if (quizViewModel.equalCurQB) {
            showFinalScore()
        }
    }
    private fun disableAnswerButtons() {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
    }
    private fun enableAnswerButtons() {
        trueButton.isEnabled = true
        falseButton.isEnabled = true
    }
    private fun showFinalScore() {
        val totalQuestions = quizViewModel.questionBankSize
        val percentage = (correctAnswersCount.toDouble() / totalQuestions.toDouble()) * 100

        val message = "Вы ответили правильно на $correctAnswersCount из $totalQuestions вопросов. Ваша оценка: %.2f%%".format(percentage)

        // Используем Snackbar для отображения результата
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}