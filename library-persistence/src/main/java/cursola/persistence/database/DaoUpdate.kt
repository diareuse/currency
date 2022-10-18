package cursola.persistence.database

import androidx.room.OnConflictStrategy
import androidx.room.Update

internal interface DaoUpdate<Type> {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(value: Type)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(values: Iterable<Type>)

}