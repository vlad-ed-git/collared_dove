package com.dev_vlad.collared_doves.models.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dev_vlad.collared_doves.di.ApplicationScope
import com.dev_vlad.collared_doves.models.dao.PoemsDao
import com.dev_vlad.collared_doves.models.entities.Poems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider


const val DATABASE_NAME = "collared_dove_db"
@Database(entities = [Poems::class], version = 1, exportSchema = false)
abstract class CollaredDoveDb : RoomDatabase() {
    abstract fun poemsDao() : PoemsDao

    //default data
    //use Provide so the injection happens lazily
    class CollaredDoveDbCreateCallback
    @Inject constructor(
        private val database : Provider<CollaredDoveDb>,
        @ApplicationScope private val appScope: CoroutineScope
    ): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val poemsDao = database.get().poemsDao()

            appScope.launch {
                poemsDao.insert(
                    Poems(
                        poemId = 1,
                        title = "roses are red, make something new!",
                        body = "hold to select, or just click to view\nyours will be great, so make something new\nplease delete this now, though you can edit it too"
                    )
                )
            }


        }
    }
}