package cursola.rate.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface DaoCreate<Model> {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(model: Model)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(models: Iterable<Model>)

}