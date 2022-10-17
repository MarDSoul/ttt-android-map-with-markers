package app.mardsoul.mapmarkers

import android.app.Application
import android.content.Context
import androidx.room.Room
import app.mardsoul.mapmarkers.data.MarkerUseCaseImpl
import app.mardsoul.mapmarkers.database.AppDatabase
import app.mardsoul.mapmarkers.domain.MarkerUseCase

class App : Application() {
    val markerUseCase: MarkerUseCase by lazy { MarkerUseCaseImpl(database.getAppDatabaseDao()) }

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "app_database.db").build()
    }
}

val Context.app: App
    get() = applicationContext as App