package cursola.persistence.database

import androidx.room.Delete

internal interface DaoDelete<Type> {

    @Delete
    suspend fun delete(value: Type)

    @Delete
    suspend fun delete(value: Iterable<Type>)

}