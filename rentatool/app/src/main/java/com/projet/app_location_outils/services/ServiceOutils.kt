package com.projet.app_location_outils.services

import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.entitées.Utilisateur
import com.projet.app_location_outils.sourceDeDonnées.ISourceOutilsHTTP
import com.projet.app_location_outils.sourceDeDonnées.SourceOutilsBidon
import com.projet.app_location_outils.sourceDeDonnées.SourceOutilsHTTP

class ServiceOutils(
    var sourceOutilsBidon: SourceOutilsBidon = SourceOutilsBidon(),
    var sourceOutilsHTTP: ISourceOutilsHTTP = SourceOutilsHTTP()
) {

    suspend fun getAllOutilsHTTP(
        filtreTrier: Int,
        filtreDate: Int,
        filtrePrix:Int,
        filtreCategorie: String,
        filtreDistance: Int,
        geolocalisation: Pair<Double, Double>) : ArrayList<Outil> {

        return sourceOutilsHTTP.obtenirOutils(
            filtreTrier,
            filtreDate,
            filtrePrix,
            filtreCategorie,
            filtreDistance,
            geolocalisation)
    }

    suspend fun getOutilByNom(string : String) : ArrayList<Outil> {
        return sourceOutilsHTTP.chercherOutils(string)
    }

    suspend fun getOutilById(id : Int) : Outil {
        return sourceOutilsHTTP.chercherOutilsParId(id)
    }

    suspend fun getCategories(): ArrayList<String> {
        val categoriesSet = mutableSetOf<String>()
        categoriesSet.addAll(sourceOutilsHTTP.obtenirCategories())
        return ArrayList(categoriesSet)
    }

    fun getAllOutils() : ArrayList<Outil> {
        return sourceOutilsBidon.getOutilsBidon()
    }

    fun getFournisseur(): List<Utilisateur> {
        return sourceOutilsBidon.getUtilisateursBidons()
    }

    suspend fun ajouterOutilHttp(outil: Outil){
        sourceOutilsHTTP.ajouterOutil(outil)
    }

    fun changerDisponibilité(idOutil: Int){
        sourceOutilsBidon.modifierDisponibilité(idOutil)
    }

    suspend fun obtenirUtilisateur(idUtilisateur: Int): Utilisateur {
        return sourceOutilsHTTP.obtenirUtilisateur(idUtilisateur)
    }

    suspend fun getOutilsParUtilisateurHTTP(idUtilisateur: Int): List<Outil> {
        return sourceOutilsHTTP.obtenirOutilsParUtilisateur(idUtilisateur)
    }

}