package cursola.rate.database

import androidx.room.OnConflictStrategy
import androidx.room.Update

interface DaoUpdate<Model> {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(model: Model)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(model: Iterable<Model>)

}