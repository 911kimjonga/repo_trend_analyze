package com.repo.integration.naver.datalab.enums

enum class Ages(
    val code: String,
    val range: IntRange,
) {
    CODE_1("1", 0..12),
    CODE_2("2", 13..18),
    CODE_3("3", 19..24),
    CODE_4("4", 25..29),
    CODE_5("5", 30..34),
    CODE_6("6", 35..39),
    CODE_7("7", 40..44),
    CODE_8("8", 45..49),
    CODE_9("9", 50..54),
    CODE_10("10", 55..59),
    CODE_11("11", 60..Int.MAX_VALUE),
    ;

    companion object {
        fun getCode(age: Int): String? {
            return entries.find { age in it.range }?.code
        }
    }
}