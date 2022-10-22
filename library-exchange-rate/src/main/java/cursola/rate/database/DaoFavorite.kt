package cursola.rate.database

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface DaoFavorite :
    DaoCreate<FavoriteCurrency>,
    DaoUpdate<FavoriteCurrency>,
    DaoDelete<FavoriteCurrency> {

    @Query("select * from favorites order by priority desc")
    suspend fun get(): List<FavoriteCurrency>

}