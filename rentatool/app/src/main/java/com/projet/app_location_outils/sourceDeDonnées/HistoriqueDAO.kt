package com.projet.app_location_outils.sourceDeDonnées

import android.content.ContentValues
import com.projet.app_location_outils.entitées.Historique

class HistoriqueDAO(private val helper: HistoriqueDatabaseHelper) : IHistoriqueDAO {

    override fun getAllHistorique(): ArrayList<Historique> {
        val historiqueListe = ArrayList<Historique>()
        val db = helper.readableDatabase
        val query = "SELECT * FROM ${HistoriqueDatabaseHelper.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(HistoriqueDatabaseHelper.COLUMN_ID))
            val motRecherche = cursor.getString(cursor.getColumnIndexOrThrow(
                HistoriqueDatabaseHelper.COLUMN_MOT
            ))

            val historique = Historique(id, motRecherche)
            historiqueListe.add(historique)
        }
        cursor.close()
        db.close()
        return historiqueListe
    }

    override fun insertHistorique(historique: Historique) {
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            put(HistoriqueDatabaseHelper.COLUMN_MOT, historique.motRecherche)
        }
        db.insert(HistoriqueDatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    override fun deleteAllHistorique() {
        val db = helper.writableDatabase
        db.delete(HistoriqueDatabaseHelper.TABLE_NAME, null, null)
        db.close()
    }
}