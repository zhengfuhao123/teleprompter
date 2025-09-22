package com.zfh.teleprompter.db.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FloatText(
    @PrimaryKey val text: String,
    @ColumnInfo(name = "create_time") val createTime: Long
)