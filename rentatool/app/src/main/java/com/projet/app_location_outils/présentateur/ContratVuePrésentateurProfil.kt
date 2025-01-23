package com.projet.app_location_outils.présentateur

import com.projet.app_location_outils.entitées.Outil

interface ContratVuePrésentateurProfil {


    interface ProfilUtilisateurContract {

        interface IView {
            fun afficherInformation(nom: String, telephone: String, email: String)
            fun afficherImageUtilisateur(imageRes: Int)
            fun afficherOutils(outils: List<Outil>)
            fun afficherErreur(message: String)

        }

        interface Presenter {
            fun chargerUtilisateur()
            fun chargerOutils()
        }
    }
}