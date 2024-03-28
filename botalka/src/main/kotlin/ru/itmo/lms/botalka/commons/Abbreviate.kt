package ru.itmo.lms.botalka.commons

import org.apache.commons.lang3.StringUtils

fun String.abbreviated(maxLength: Int = 8): String =
    StringUtils.abbreviate(this, maxLength)
