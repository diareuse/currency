package cursola.rate.database

import androidx.room.Delete

interface DaoDelete<Model> {

    @Delete
    suspend fun delete(model: Model)

    @Delete
    suspend fun delete(model: Iterable<Model>)

}