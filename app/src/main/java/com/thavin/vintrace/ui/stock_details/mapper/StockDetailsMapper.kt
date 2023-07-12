package com.thavin.vintrace.ui.stock_details.mapper

import com.thavin.vintrace.R
import java.text.DecimalFormat
import java.text.NumberFormat

const val NUMBER_FORMAT_PATTERN = "#,###"

fun List<String>.toResources(): List<Int> =
    if (this.isEmpty()) {
        listOf(R.drawable.img_generic)
    } else {
        this.map {
            when (it) {
                ImageTypes.WINE1.endpoint -> {
                    R.drawable.img_wine_flowers
                }

                ImageTypes.WINE2.endpoint -> {
                    R.drawable.img_wine_grapes
                }

                ImageTypes.WINE3.endpoint -> {
                    R.drawable.img_wine_strawberry
                }

                ImageTypes.WINE4.endpoint -> {
                    R.drawable.img_wine_table
                }

                else -> {
                    R.drawable.img_generic
                }
            }
        }
    }

fun Int.numberFormat(): String {
    val formatter: NumberFormat = DecimalFormat(NUMBER_FORMAT_PATTERN)
    return formatter.format(this)
}
