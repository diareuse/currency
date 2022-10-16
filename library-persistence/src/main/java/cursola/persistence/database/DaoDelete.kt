package cursola.persistence.database

import androidx.room.Delete

interface DaoDelete<Type> {

    @Delete
    suspend fun delete(value: Type)

    @Delete
    suspend fun delete(value: Iterable<Type>)

}