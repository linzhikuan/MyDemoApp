package com.lzk.lettin.business.main.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 历史开奖记录实体。
 * 号码以逗号分隔字符串存储，保持读取与写入简单。
 */
@Entity(
    tableName = "lottery_draw",
    indices = [Index(value = ["type", "issueNo"], unique = true)],
)
data class LotteryDraw(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    /** 彩种: SSQ / DLT */
    val type: String,
    /** 期号，如 "2024001" */
    val issueNo: String,
    /** 开奖日期，如 "2024-01-02" */
    val date: String,
    /** 前区号码，逗号分隔，升序 */
    val frontNumbers: String,
    /** 后区号码，逗号分隔，升序 */
    val backNumbers: String,
    /** 奖池金额，可为空 */
    val poolAmount: Long? = null,
    /** 一等奖注数 */
    val firstCount: Int? = null,
    /** 一等奖单注奖金 */
    val firstPrize: Long? = null,
    /** 二等奖注数 */
    val secondCount: Int? = null,
    /** 二等奖单注奖金 */
    val secondPrize: Long? = null,
)

fun String.parseNumbers(): List<Int> =
    split(',').mapNotNull { it.trim().toIntOrNull() }

fun List<Int>.toNumberString(): String =
    sorted().joinToString(",")
