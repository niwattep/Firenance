package com.hobby.niwat.firenance

import java.util.*


class Transaction(
		val amount: Int = 0,
		val type: Int = 0,
		val note: String = "",
		val day: Int = 0,
		val month: Int = 0,
		val year: Int = 0,
		val timeStamp: Date = Date()) {

	companion object {
		const val TYPE_INCOME = 1
		const val TYPE_EXPENSE = 2
		const val AMOUNT = "amount"
		const val TYPE = "type"
		const val NOTE = "note"
		const val DAY = "day"
		const val MONTH = "month"
		const val YEAR = "year"
		const val TIMESTAMP = "timeStamp"
	}
}