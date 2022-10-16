package cursola.persistence.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface DaoCreate<Type> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(value: Type)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(values: Iterable<Type>)

}