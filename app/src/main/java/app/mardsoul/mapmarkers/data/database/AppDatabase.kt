package app.mardsoul.mapmarkers.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PlaceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAppDatabaseDao(): AppDatabaseDao
}