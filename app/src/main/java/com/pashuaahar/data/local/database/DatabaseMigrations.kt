package com.pashuaahar.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE recipes ADD COLUMN createdAtMillis INTEGER NOT NULL DEFAULT 0"
            )
        }
    }
}
