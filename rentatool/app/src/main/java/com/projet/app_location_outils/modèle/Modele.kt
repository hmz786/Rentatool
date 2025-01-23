package com.projet.app_location_outils.modèle

import com.projet.app_location_outils.entitées.Historique
import com.projet.app_location_outils.entitées.Outil
import com.projet.app_location_outils.entitées.Utilisateur
import com.projet.app_location_outils.services.ServiceHistorique
import com.projet.app_location_outils.services.ServiceOutils

class Modele private constructor(
    val serviceOutils: ServiceOutils = ServiceOutils(),
    val serviceHistorique: ServiceHistorique = ServiceHistorique()
) : IModele {

    override var outilActuel : Outil? = null
    override var filtreTrier = 1
    override var filtreDate = 1
    override var filtrePrix = 1
    override var filtreCategorie = ""
    override var filtreDistance = 0

    companion object {
        val instance: Modele = Modele()
    }

    override suspend fun getAllOutilsHTTP(geolocalisation: Pair<Double, Double>) : ArrayList<Outil> {
        return serviceOutils.getAllOutilsHTTP(filtreTrier, filtreDate, filtrePrix, filtreCategorie, filtreDistance, geolocalisation)
    }

    override suspend fun getOutilByNom(string : String) : ArrayList<Outil> {
        return serviceOutils.getOutilByNom(string)
    }

    override suspend fun getOutilById( id : Int ) {
        outilActuel = serviceOutils.getOutilById(id)
    }

    override suspend fun getCategories(): ArrayList<String> {
        return serviceOutils.getCategories()
    }

    override fun getAllHistorique() : ArrayList<Historique> {
        return serviceHistorique.getAllHistorique()
    }

    override fun insertHistorique(historique: Historique) {
        serviceHistorique.insertHistorique(historique)
    }

    override fun deleteAllHistorique() {
        serviceHistorique.deleteAllHistorique()
    }

    fun getAllOutils() : ArrayList<Outil> {
        return serviceOutils.getAllOutils()
    }

    fun getFournisseur(): List<Utilisateur> {
        return serviceOutils.getFournisseur()
    }

    override suspend fun ajouterOutil(outil: Outil){
        serviceOutils.ajouterOutilHttp(outil)
    }

    override fun changerDisponibilité(){
        outilActuel?.let { serviceOutils.changerDisponibilité(it.id) }
    }
    override suspend fun obtenirUtilisateur(idUtilisateur: Int): Utilisateur {
        return serviceOutils.obtenirUtilisateur(idUtilisateur)
    }

    override suspend fun getOutilsParUtilisateur(idUtilisateur: Int): List<Outil> {
        return serviceOutils.getOutilsParUtilisateurHTTP(idUtilisateur)
    }

}