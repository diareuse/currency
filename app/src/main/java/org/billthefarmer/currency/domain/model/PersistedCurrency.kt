package org.billthefarmer.currency.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.billthefarmer.currency.domain.adapter.DatabaseTypeAdapter
import java.util.*

@Entity(tableName = "currencies")
@TypeConverters(DatabaseTypeAdapter::class)
data class PersistedCurrency(
    @PrimaryKey
    @ColumnInfo(name = "currency") val currency: Currency
)