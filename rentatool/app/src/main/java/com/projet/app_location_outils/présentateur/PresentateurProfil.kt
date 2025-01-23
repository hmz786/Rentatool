package com.projet.app_location_outils.présentateur

import android.view.View
import com.projet.app_location_outils.R
import com.projet.app_location_outils.modèle.Modele
import com.projet.app_location_outils.entitées.Utilisateur
import  com.projet.app_location_outils.présentateur.ContratVuePrésentateurProfil.ProfilUtilisateurContract.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PresentateurProfil(private val vue: ContratVuePrésentateurProfil.ProfilUtilisateurContract.IView) : Presenter {

    private val  modele = Modele.instance
    override fun chargerUtilisateur() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val utilisateur = modele.obtenirUtilisateur(1)
                withContext(Dispatchers.Main) {
                    vue.afficherInformation("${utilisateur.nom} ${utilisateur.prenom}", utilisateur.numeroTelephone, utilisateur.courriel)
                    vue.afficherImageUtilisateur(utilisateur.imageUtilisateur)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    vue.afficherErreur("Erreur lors du chargement des informations utilisateur : ${e.message}")
                }
            }
        }
    }


    override fun chargerOutils() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Récupérer les outils dynamiquement depuis l'API
                val outils = modele.getOutilsParUtilisateur(1)
                withContext(Dispatchers.Main) {
                    vue.afficherOutils(outils)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    vue.afficherErreur("Erreur lors du chargement des outils : ${e.message}")
                }
            }
        }
    }
}
