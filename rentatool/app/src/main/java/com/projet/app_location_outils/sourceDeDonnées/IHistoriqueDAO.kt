package com.projet.app_location_outils.sourceDeDonnées

import com.projet.app_location_outils.entitées.Historique

interface IHistoriqueDAO {
    fun getAllHistorique(): ArrayList<Historique>
    fun insertHistorique(historique: Historique)
    fun deleteAllHistorique()
}