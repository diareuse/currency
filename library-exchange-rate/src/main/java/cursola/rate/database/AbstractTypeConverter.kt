package cursola.rate.database

import androidx.room.TypeConverter

abstract class AbstractTypeConverter<Type, Primitive> {

    @TypeConverter
    abstract fun to(type: Type): Primitive

    @TypeConverter
    abstract fun from(primitive: Primitive): Type

}