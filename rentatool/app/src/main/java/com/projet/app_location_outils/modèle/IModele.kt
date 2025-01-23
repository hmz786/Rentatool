package com.projet.app_location_outils.modèle

import android.location.Location
import com.projet.app_location_outils.entitées.Historique
import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.entitées.Utilisateur

interface IModele {
    var filtreTrier: Int
    var filtreDate: Int
    var filtrePrix: Int
    var filtreCategorie: String
    var filtreDistance: Int
    var outilActuel: Outil?
    suspend fun getAllOutilsHTTP(geolocalisation: Pair<Double, Double>) : ArrayList<Outil>
    suspend fun getOutilByNom(string : String) : ArrayList<Outil>
    suspend fun getOutilById( id : Int)
    suspend fun getCategories() : ArrayList<String>
    suspend fun obtenirUtilisateur(idUtilisateur: Int): Utilisateur
    suspend fun getOutilsParUtilisateur(idUtilisateur: Int): List<Outil>
    fun getAllHistorique() : ArrayList<Historique>
    fun insertHistorique(historique: Historique)
    fun deleteAllHistorique()
    suspend fun ajouterOutil(outil: Outil)
}