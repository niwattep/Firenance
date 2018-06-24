package com.hobby.niwat.firenance.model

import java.util.*


class Transaction(
		val amount: Int = 0,
		val note: String = "",
		val day: Int = 0,
		val month: Int = 0,
		val year: Int = 0,
		val timestamp: Date = Date(),
		val group: Int = 0) {

	companion object {
		const val AMOUNT = "amount"
		const val NOTE = "note"
		const val DAY = "day"
		const val MONTH = "month"
		const val YEAR = "year"
		const val TIMESTAMP = "timestamp"
		const val GROUP = "group"
	}
}