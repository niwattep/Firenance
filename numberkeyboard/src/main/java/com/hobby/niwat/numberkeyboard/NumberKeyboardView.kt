package com.hobby.niwat.numberkeyboard

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_number_keyboard.view.*

class NumberKeyboardView : FrameLayout, View.OnClickListener {
	var mDoneClickListener: OnDoneClickListener? = null
	var mBackSpaceClickListener: OnBackSpaceClickListener? = null
	var mNumberClickListener: OnNumberClickListener? = null

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

	override fun onClick(v: View?) {
		v?.let {
			val id = it.id
			when {
				getNumberButtonViewIds().any { it == id } -> {
					val number = getNumberButtonViewIds().indexOf(id)
					mNumberClickListener?.onNumberClick(number)
				}
				id == R.id.backSpaceButton -> mBackSpaceClickListener?.onBackSpaceClick()
				else -> mDoneClickListener?.onDoneClick()
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

	interface OnDoneClickListener {
		fun onDoneClick()
	}

	interface OnBackSpaceClickListener {
		fun onBackSpaceClick()
	}

	interface OnNumberClickListener {
		fun onNumberClick(number: Int)
	}
}