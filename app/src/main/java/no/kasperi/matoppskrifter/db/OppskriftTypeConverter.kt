package no.kasperi.matoppskrifter.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class OppskriftTypeConverter {
    @TypeConverter
    fun fraAnyTilString(attribute:Any?) : String{
        if(attribute == null)
            return ""
            return attribute as String
    }
        @TypeConverter
        fun fraStringTilAny(attribute: String?):Any{
            if(attribute == null)
                return ""
                return attribute
    }
}


