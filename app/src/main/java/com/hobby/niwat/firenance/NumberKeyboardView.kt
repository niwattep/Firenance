package com.hobby.niwat.firenance

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_number_keyboard.view.*

class NumberKeyboardView : FrameLayout, View.OnClickListener {
	private var onKeyPressListener: OnKeyPressedListener? = null

	constructor(context: Context) : super(context)
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

	init {
		inflate(context, R.layout.view_number_keyboard, this)
		setButtonListener()
	}

	private fun setButtonListener() {
		buttonOne.setOnClickListener(this)
		buttonTwo.setOnClickListener(this)
		buttonThree.setOnClickListener(this)
		buttonFour.setOnClickListener(this)
		buttonFive.setOnClickListener(this)
		buttonSix.setOnClickListener(this)
		buttonSeven.setOnClickListener(this)
		buttonEight.setOnClickListener(this)
		buttonNine.setOnClickListener(this)
		buttonZero.setOnClickListener(this)
		doneButton.setOnClickListener(this)
		backSpaceButton.setOnClickListener(this)
	}

	fun setOnKeyPressedListener(listener: OnKeyPressedListener) {
		onKeyPressListener = listener
	}

	override fun onClick(v: View?) {
		v?.id?.let {id ->
			when {
				getNumberButtonViewIds().any { it == id } -> {
					val number = getNumberButtonViewIds().indexOf(id)
					onKeyPressListener?.onNumberPressed(number)
				}
				id == R.id.backSpaceButton -> onKeyPressListener?.onBackSpacePressed()
				else -> onKeyPressListener?.onDonePressed()
			}
		}
	}

	private fun getNumberButtonViewIds(): List<Int> {
		return listOf(
				R.id.buttonZero,
				R.id.buttonOne,
				R.id.buttonTwo,
				R.id.buttonThree,
				R.id.buttonFour,
				R.id.buttonFive,
				R.id.buttonSix,
				R.id.buttonSeven,
				R.id.buttonEight,
				R.id.buttonNine
		)
	}

	interface OnKeyPressedListener {
		fun onDonePressed()

		fun onBackSpacePressed()

		fun onNumberPressed(number: Int)
	}
}