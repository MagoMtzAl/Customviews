package com.mago.customviews.util

/**
 * @author by jmartinez
 * @since 15/01/2020.
 */

object Regex {
    const val A_TO_Z_WITH_BLANK_SPACES = "[A-ZÁ-ß ]+"
    const val A_TO_Z = "[A-ZÁ-ß]+"
    const val ONLY_NUMBERS = "[0-9]+"
    const val DATE = "[^\\d.]|\\."
}