package com.hobby.niwat.firenance.model

import java.io.Serializable

class TransactionGroup(
		val name: String? = "",
		val actual: Int? = 0,
		val target: Int? = 0,
		val actualText: String? = "",
		val targetText: String? = "",
		val value: Int? = -1) : Serializable {

	companion object {
		const val NAME = "name"
		const val ACTUAL = "actual"
		const val TARGET = "target"
		const val ACTUAL_TEXT = "actualText"
		const val TARGET_TEXT = "targetText"
		const val VALUE = "value"
	}
}