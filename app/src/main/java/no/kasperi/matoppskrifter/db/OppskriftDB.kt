package no.kasperi.matoppskrifter.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import no.kasperi.matoppskrifter.pojo.MealDB

@Database(entities = [MealDB::class], version = 6)
abstract class OppskriftDB : RoomDatabase() {
    abstract fun oppskriftDao(): OppskriftDao

    companion object {
        @Volatile
        var INSTANCE:OppskriftDB? = null

        @Synchronized
        fun hentInstance(context: Context): OppskriftDB{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    OppskriftDB::class.java,
                    "oppskrift.db",
                ).fallbackToDestructiveMigration()
                    .build()

            }
            return INSTANCE as OppskriftDB
        }
    }
}