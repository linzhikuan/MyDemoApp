package com.lzk.lettin.business.main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 保存的选号记录（用户保存、预测结果等）。
 */
@Entity(tableName = "saved_ticket")
data class SavedTicket(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val frontNumbers: String,
    val backNumbers: String,
    /** 记录来源：随机机选 / 自选 / 预测-冷热 / 预测-遗漏 / 预测-综合 / 胆拖 / 复式 */
    val source: String,
    /** 创建时间(ms) */
    val createdAt: Long,
)
