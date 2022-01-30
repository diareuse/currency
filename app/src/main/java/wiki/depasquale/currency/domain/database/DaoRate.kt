package wiki.depasquale.currency.domain.database

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import wiki.depasquale.currency.domain.model.PersistedRate

@Dao
interface DaoRate {

    @WorkerThread
    @Query("SELECT * FROM rates WHERE rates.time <= :end AND rates.time >= :start ORDER BY rates.time DESC, rates.currency ASC")
    fun getRatesInRange(start: Long, end: Long): List<PersistedRate>

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: PersistedRate)

}