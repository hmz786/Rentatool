package com.projet.app_location_outils.sourceDeDonnées

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.projet.app_location_outils.AppLocationOutils

class HistoriqueDatabaseHelper: SQLiteOpenHelper(AppLocationOutils.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Historique.db"
        const val TABLE_NAME = "HistoriqueRecherche"
        const val COLUMN_ID = "id"
        const val COLUMN_MOT = "motRecherche"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_MOT TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

}