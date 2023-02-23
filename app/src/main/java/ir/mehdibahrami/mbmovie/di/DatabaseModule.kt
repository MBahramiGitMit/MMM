package ir.mehdibahrami.mbmovie.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.mehdibahrami.mbmovie.model.db.FavoriteMovieDao
import ir.mehdibahrami.mbmovie.model.db.MovieDatabase
import ir.mehdibahrami.mbmovie.util.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, MovieDatabase::class.java, Constants.DB_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideFavoriteMovieDao(db: MovieDatabase): FavoriteMovieDao = db.favoriteMovieDao()
}