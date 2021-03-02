package com.dev_vlad.collared_doves.di

import android.app.Application
import androidx.room.Room
import com.dev_vlad.collared_doves.CollaredDovesApp
import com.dev_vlad.collared_doves.models.dao.PoemsDao
import com.dev_vlad.collared_doves.models.database.CollaredDoveDb
import com.dev_vlad.collared_doves.models.database.DATABASE_NAME
import com.dev_vlad.collared_doves.models.repo.poems.PoemsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDb(
        app: Application,
        createCallback: CollaredDoveDb.CollaredDoveDbCreateCallback
    ): CollaredDoveDb {
       return Room.databaseBuilder(
            app.applicationContext,
            CollaredDoveDb::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
           .addCallback(createCallback)
           .build()
    }

    @Provides
    //dao-s are already singleton
    fun providePoemsDao(db : CollaredDoveDb) : PoemsDao{
        return db.poemsDao()
    }

    @Provides
    @Singleton
    fun providePoemsRepo(poemsDao: PoemsDao) : PoemsRepo{
        return PoemsRepo(poemsDao)
    }

    @ApplicationScope
    @Provides
    @Singleton
    fun provideAppCoroutineScope() : CoroutineScope{
        //supervisor job says keep other child running if one fails, do not cancel whole scope
        return CoroutineScope(SupervisorJob())
    }

}
//custom annotation to explicitly specify the scope of a coroutine
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope