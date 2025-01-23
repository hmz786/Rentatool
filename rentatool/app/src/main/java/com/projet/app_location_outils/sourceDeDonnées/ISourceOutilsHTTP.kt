package com.projet.app_location_outils.sourceDeDonnées

import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.entitées.Utilisateur

interface ISourceOutilsHTTP {
    suspend fun obtenirOutils(
        filtreTrier: Int,
        filtreDate: Int,
        filtrePrix:Int,
        filtreCategorie: String,
        filtreDistance: Int,
        localisation: Pair<Double, Double>
    ) : ArrayList<Outil>
    suspend fun chercherOutils( string: String ) : ArrayList<Outil>
    suspend fun chercherOutilsParId( id : Int) : Outil
    suspend fun obtenirCategories(): ArrayList<String>

    suspend fun obtenirUtilisateur(idUtilisateur: Int): Utilisateur
    suspend fun obtenirOutilsParUtilisateur(idUtilisateur: Int): List<Outil>

    suspend fun ajouterOutil(outil: Outil): Boolean


}
