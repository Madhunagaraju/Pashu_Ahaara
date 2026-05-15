package com.pashuaahar.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pashuaahar.data.local.dao.CowDao
import com.pashuaahar.data.local.dao.GrainDao
import com.pashuaahar.data.local.dao.RecipeDao
import com.pashuaahar.data.local.database.DatabaseMigrations
import com.pashuaahar.data.local.database.PashuAaharDatabase
import com.pashuaahar.data.repository.AuthRepositoryImpl
import com.pashuaahar.data.repository.CowRepositoryImpl
import com.pashuaahar.data.repository.GrainRepositoryImpl
import com.pashuaahar.data.repository.RecipeRepositoryImpl
import com.pashuaahar.domain.repository.AuthRepository
import com.pashuaahar.domain.repository.CowRepository
import com.pashuaahar.domain.repository.GrainRepository
import com.pashuaahar.domain.repository.RecipeRepository
import com.pashuaahar.util.BookmarkStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository = AuthRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PashuAaharDatabase {
        return Room.databaseBuilder(
            context,
            PashuAaharDatabase::class.java,
            "pashu_aahar.db"
        )
            .addMigrations(DatabaseMigrations.MIGRATION_1_2)
            .fallbackToDestructiveMigration(true)
            .addCallback(object : androidx.room.RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.execSQL(
                        """
                        INSERT OR REPLACE INTO grains(id, name, proteinPercent, tdnPercent, pricePerKg, isDefault)
                        VALUES
                            ('maize', 'Maize', 9.0, 80.0, 25.0, 1),
                            ('cottonseed_cake', 'Cottonseed Cake', 23.0, 75.0, 35.0, 1),
                            ('mustard_cake', 'Mustard Cake', 38.0, 70.0, 40.0, 1),
                            ('wheat_bran', 'Wheat Bran', 15.0, 65.0, 20.0, 1),
                            ('rice_bran', 'Rice Bran', 12.0, 70.0, 18.0, 1),
                            ('soybean_meal', 'Soybean Meal', 45.0, 85.0, 55.0, 1)
                        """.trimIndent()
                    )
                }
            })
            .build()
    }

    @Provides
    fun provideCowDao(database: PashuAaharDatabase): CowDao = database.cowDao()

    @Provides
    fun provideGrainDao(database: PashuAaharDatabase): GrainDao = database.grainDao()

    @Provides
    fun provideRecipeDao(database: PashuAaharDatabase): RecipeDao = database.recipeDao()

    @Provides
    @Singleton
    fun provideCowRepository(cowDao: CowDao): CowRepository = CowRepositoryImpl(cowDao)

    @Provides
    @Singleton
    fun provideGrainRepository(grainDao: GrainDao): GrainRepository = GrainRepositoryImpl(grainDao)

    @Provides
    @Singleton
    fun provideRecipeRepository(recipeDao: RecipeDao): RecipeRepository = RecipeRepositoryImpl(recipeDao)

    @Provides
    @Singleton
    fun provideBookmarkStore(@ApplicationContext context: Context): BookmarkStore = BookmarkStore(context)
}
