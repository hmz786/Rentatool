package com.projet.app_location_outils.services

import com.projet.app_location_outils.entitées.Historique
import com.projet.app_location_outils.sourceDeDonnées.HistoriqueDAO
import com.projet.app_location_outils.sourceDeDonnées.HistoriqueDatabaseHelper
import com.projet.app_location_outils.sourceDeDonnées.IHistoriqueDAO


class ServiceHistorique(val historiqueDAO: IHistoriqueDAO = HistoriqueDAO(HistoriqueDatabaseHelper())) {

    fun getAllHistorique() : ArrayList<Historique> {
        return historiqueDAO.getAllHistorique()
    }

    fun insertHistorique(historique: Historique) {
        historiqueDAO.insertHistorique(historique)
    }

    fun deleteAllHistorique() {
        historiqueDAO.deleteAllHistorique()
    }
}