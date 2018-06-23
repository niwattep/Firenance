package com.hobby.niwat.firenance.model

class TransactionGroup(
		val name: String? = "",
		val actual: Int? = 0,
		val target: Int? = 0,
		val actualText: String? = "",
		val targetText: String? = "",
		val value: Int? = -1
) {
	companion object {
		const val NAME = "name"
		const val ACTUAL = "actual"
		const val TARGET = "target"
		const val ACTUAL_TEXT = "actualText"
		const val TARGET_TEXT = "targetText"
		const val VALUE = "value"
	}
}